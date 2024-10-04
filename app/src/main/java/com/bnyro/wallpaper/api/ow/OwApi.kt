package com.bnyro.wallpaper.api.ow

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitHelper
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class OwApi : Api() {
    override val name = "OWalls"
    override val baseUrl = "https://gist.github.com/"
    override val icon = Icons.Default.Air

    override val filters: Map<String, List<String>> = mapOf(
        "style" to listOf("all", "light", "dark")
    )

    private val api = RetrofitHelper.create(baseUrl, OWalls::class.java)
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
        api.getAll().forEach { _, images ->
            images.jsonObject.forEach { category, source ->
                wallpapers.add(0, Wallpaper(imgSrc = source.jsonPrimitive.content, category = category))
            }
        }
    }

    override suspend fun getRandomWallpaperUrl(): String? {
        if (wallpapers.isEmpty()) loadAll()
        return filteredWallpapers.randomOrNull()?.imgSrc
    }
}
