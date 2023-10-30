package com.bnyro.wallpaper.api.px

import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitBuilder

class PxApi : Api() {
    override val name: String = "Google Pixel"
    override val baseUrl: String = "https://api.github.com"
    private val api = RetrofitBuilder.create(baseUrl, Pixel::class.java)
    private val imgSrcPrefix = "https://raw.githubusercontent.com/wacko1805/Pixel-Wallpapers/main/"

    private var wallpapers: List<Wallpaper> = emptyList()
    private val resultsPerPage = 20

    private suspend fun updateWallpaperList() {
        wallpapers = api.getWallpapers()
            .tree
            .filter { it.path.endsWith(".jpg") || it.path.endsWith(".png") }
            .map {
                Wallpaper(
                    title = it.path.split("/").last().split(".").first(),
                    imgSrc = imgSrcPrefix + it.path,
                    category = it.path.split("/").first(),
                    fileSize = it.size
                )
            }
    }

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        if (wallpapers.isEmpty()) {
            updateWallpaperList()
        }
        return wallpapers.subList((page - 1) * resultsPerPage, page * resultsPerPage)
    }

    override suspend fun getRandomWallpaperUrl(): String? {
        if (wallpapers.isEmpty()) updateWallpaperList()
        return wallpapers.randomOrNull()?.imgSrc
    }
}