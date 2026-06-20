package net.youapps.wallpaper_apis.im

import com.fleeksoft.ksoup.Ksoup
import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper
import net.youapps.wallpaper_apis.WallpaperApi

class ImgthApi: WallpaperApi() {
    override val name: String = "Imgth"
    override val baseUrl: String = "https://imgth.com"

    override var communityName: String? = "1022/high-definition-wallpapers"
    override val requiresCommunityName: Boolean = true

    private val api: Imgth by lazy {
        RetrofitHelper.create(baseUrl)
    }

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        val response = if (communityName.isNullOrBlank()) {
            api.getLive()
        } else {
            api.getCategory(communityName!!, page - 1)
        }
        val doc = Ksoup.parse(response.string())

        return doc.select("a.thumbnail").map {
            val thumbnail = it.select("img").attr("src")
                .replace("http://", "https://")
            Wallpaper(
                imgSrc = thumbnail.replace("/thumbs/", "/images/"),
                thumb = thumbnail,
                url = baseUrl + it.attr("href")
            )
        }
    }
}