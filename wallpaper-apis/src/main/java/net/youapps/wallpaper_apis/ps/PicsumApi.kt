package net.youapps.wallpaper_apis.ps

import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper
import net.youapps.wallpaper_apis.WallpaperApi

class PicsumApi : WallpaperApi() {
    override val name: String = "Picsum"
    override val baseUrl: String = "https://picsum.photos"
    private val maxPage = 34

    private val api = RetrofitHelper.create<Picsum>(baseUrl)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        return api.getWallpapers(maxPage + 1 - page).map {
            Wallpaper(
                imgSrc = it.downloadUrl!!,
                author = it.author,
                resolution = "${it.width}x${it.height}",
                url = it.url
            )
        }
    }

    override suspend fun getRandomWallpaperUrl(): String {
        return "$baseUrl/1080/1920"
    }
}
