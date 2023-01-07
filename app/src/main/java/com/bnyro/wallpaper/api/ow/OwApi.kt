package com.bnyro.wallpaper.api.ow

import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.ext.toJsonObject
import com.bnyro.wallpaper.util.RetrofitBuilder

class OwApi() : Api() {
    override val name: String = "OWalls"
    override val baseUrl: String = "https://gist.github.com/"
    override val filters: Map<String, List<String>> = mapOf()
    override val supportsTags: Boolean = false

    private val api = RetrofitBuilder.create(baseUrl, OWalls::class.java)
    private val resultsPerPage = 20

    private var wallpapers: MutableList<Wallpaper> = mutableListOf()

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        if (wallpapers.isEmpty()) getAll()
        return if (resultsPerPage * 20 > wallpapers.size) {
            wallpapers.toList().subList(
                wallpapers.size - (wallpapers.size % resultsPerPage),
                wallpapers.size
            )
        } else {
            wallpapers.toList().subList((page - 1) * resultsPerPage, page * resultsPerPage)
        }
    }

    suspend fun getAll() {
        val response = api.getAll().toJsonObject()
        response.fields().forEach { timePair ->
            timePair.value.fields().forEach {
                wallpapers.add(
                    Wallpaper(
                        imgSrc = it.value.textValue(),
                        thumb = it.value.textValue(),
                        category = it.key
                    )
                )
            }
        }
    }

    override suspend fun getRandomWallpaperUrl(): String? {
        if (wallpapers.isEmpty()) getAll()
        return wallpapers.shuffled().firstOrNull()?.imgSrc
    }
}
