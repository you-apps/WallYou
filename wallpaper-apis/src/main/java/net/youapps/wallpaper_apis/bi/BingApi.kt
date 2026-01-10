package net.youapps.wallpaper_apis.bi

import net.youapps.wallpaper_apis.WallpaperApi
import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper

class BingApi : WallpaperApi() {
    override val name: String = "Bing"
    override val baseUrl: String = "https://www.bing.com"

    override val availableFilters: Map<String, List<String>> = mapOf(
        "resolution" to listOf("1080x1920", "768x1366", "1366x768", "1920x1080", "UHD")
    )
    private val previewResolution = "768x1366"

    val api = RetrofitHelper.create<Bing>(baseUrl)

    private fun getImgSrc(path: String, resolution: String): String {
        return "$baseUrl${path}_${resolution}.jpg"
    }

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        // doesn't support pagination, at most 7 images are available
        if (page != 1) return emptyList()
        val resolution = selectedFilters["resolution"]!!

        return api.getImages().images.map {
            Wallpaper(
                imgSrc = getImgSrc(it.urlBase, resolution),
                thumb = getImgSrc(it.urlBase, previewResolution),
                title = it.title,
                url = "$baseUrl${it.quiz}",
                resolution = resolution,
                // creation date doesn't contain any dividers by default
                creationDate = it.startDate
                    ?.replaceRange(6, 6, "-")
                    ?.replaceRange(4, 4, "-"),
                author = it.copyright
            )
        }
    }

    override suspend fun getRandomWallpaperUrl() = api.getImages(1).images
        .firstOrNull()
        ?.let {
            getImgSrc(it.urlBase, selectedFilters["resolution"]!!)
        }
}
