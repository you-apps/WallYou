package net.youapps.wallpaper_apis.wc

import com.fleeksoft.ksoup.Ksoup
import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper
import net.youapps.wallpaper_apis.WallpaperApi

class WallpaperCaveApi: WallpaperApi() {
    override val name: String = "WallpaperCave"
    override val baseUrl: String = "https://wallpapercave.com"

    override val availableFilters: Map<String, List<String>> = mapOf("page" to listOf("featured", "latest"))

    private val api: WallpaperCave by lazy {
        RetrofitHelper.create(baseUrl)
    }

    private fun parseHtml(html: String): List<Wallpaper> {
        val doc = Ksoup.parse(html)

        return doc.select("a.wpimg").map {
            val thumbSrc = baseUrl + it.select("img").attr("src")
            println(thumbSrc)

            Wallpaper(
                // "/wpr" seems to be for thumbs, "/wp" for full-res images
                imgSrc = thumbSrc.replace("/wpr/", "/wp/"),
                url = baseUrl + it.attr("href"),
                title = it.attr("title"),
                thumb = thumbSrc
            )
        }
    }

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        // page is 0-based
        val pageType = selectedFilters["page"]!!
        val responseHtml = when (pageType) {
            "featured" -> {
                api.getFeaturedWallpapers(page - 1).first().imgs
            }
            "latest" -> {
                api.getLatestWallpapers(page - 1).first().imgs
            }
            else -> {
                throw IllegalArgumentException("unsupported page type")
            }
        }

        return parseHtml(responseHtml)
    }

    override suspend fun getRandomWallpaperUrl(): String? {
        val response = api.getLatestWallpapers(0).first()
        return parseHtml(response.imgs).first().url
    }
}