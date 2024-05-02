package com.bnyro.wallpaper.api.bi

import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitHelper

class BiApi : Api() {
    override val name: String = "Bing"
    override val baseUrl: String = "https://www.bing.com"
    override val filters: Map<String, List<String>> = mapOf(
        "resolution" to listOf("1366x768", "1920x1080", "UHD")
    )
    private val previewResolution = "1366x768"

    val api = RetrofitHelper.create(baseUrl, Bing::class.java)

    private fun getImgSrc(path: String, resolution: String): String {
        return "$baseUrl${path}_${resolution}.jpg"
    }

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        // doesn't support pagination, at most 7 images are available
        if (page != 1) return emptyList()
        val resolution = getQuery("resolution")

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
                    ?.replaceRange(4, 4, "-")
            )
        }
    }

    override suspend fun getRandomWallpaperUrl() = api.getImages(1).images
        .firstOrNull()
        ?.let {
            getImgSrc(it.urlBase, getQuery("resolution"))
        }
}
