package com.bnyro.wallpaper.api.bi

import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitBuilder

class BiApi() : Api() {
    override val name: String = "Bing"
    override val baseUrl: String = "https://www.bing.com"

    val api = RetrofitBuilder.create(baseUrl, Bing::class.java)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        // doesn't support pagination, at most 7 images are available
        if (page != 1) return emptyList()

        return api.getImages().images.map {
            val imgUrl = "$baseUrl${it.url}"
            Wallpaper(
                imgSrc = imgUrl,
                title = it.title,
                url = "$baseUrl${it.quiz}",
                resolution = "1920x1080",
                // creation date doesn't contain any dividers by default
                creationDate = it.startDate
                    ?.replaceRange(6, 6, "-")
                    ?.replaceRange(4, 4, "-")
            )
        }
    }

    override suspend fun getRandomWallpaperUrl() = api.getImages(1).images
        .firstOrNull()?.url?.let { "$baseUrl$it" }
}
