package com.bnyro.wallpaper.api.ow

import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.ext.toJsonObject
import com.bnyro.wallpaper.util.RetrofitBuilder

class OwApi() : Api() {
    override val name: String = "OWalls"
    override val baseUrl: String = "https://gist.github.com/"
    override val filters: Map<String, List<String>> = mapOf(
        "style" to listOf("all", "light", "dark")
    )
    override val supportsTags: Boolean = false

    private val api = RetrofitBuilder.create(baseUrl, OWalls::class.java)
    private val resultsPerPage = 20

    private var wallpapers: MutableList<Wallpaper> = mutableListOf()
    private val filteredWallpapers: MutableList<Wallpaper>
        get() = when (val query = getQuery("style")) {
            "light" -> wallpapers.filter { it.category == query }
            "dark" -> wallpapers.filter { it.category == query }
            else -> wallpapers
        }.toMutableList()

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        if (wallpapers.isEmpty()) loadAll()
        return if (resultsPerPage * 20 > filteredWallpapers.size) {
            filteredWallpapers.toList().subList(
                filteredWallpapers.size - (filteredWallpapers.size % resultsPerPage),
                filteredWallpapers.size
            )
        } else {
            filteredWallpapers.toList().subList((page - 1) * resultsPerPage, page * resultsPerPage)
        }
    }

    private suspend fun loadAll() {
        val response = api.getAll().toJsonObject()
        response.fields().forEach { timePair ->
            timePair.value.fields().forEach {
                wallpapers.add(
                    0,
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
        if (wallpapers.isEmpty()) loadAll()
        return filteredWallpapers.randomOrNull()?.imgSrc
    }
}
