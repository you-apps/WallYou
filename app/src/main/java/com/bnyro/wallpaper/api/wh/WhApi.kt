package com.bnyro.wallpaper.api.wh

import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitBuilder

class WhApi() : Api() {
    override val baseUrl: String = "https://wallhaven.cc/api/v1/"
    private val api = RetrofitBuilder.create(baseUrl, Wallhaven::class.java)

    override suspend fun getWallpapers(): List<Wallpaper> {
        return api.search().data?.map {
            Wallpaper(
                url = it.url,
                category = it.category,
                fileSize = it.file_size,
                resolution = it.resolution,
                imgSrc = it.thumbs?.original
            )
        } ?: listOf()
    }
}
