package net.youapps.wallpaper_apis.ow

import net.youapps.wallpaper_apis.WallpaperApi
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper

class OwallsApi : WallpaperApi() {
    override val name = "OWalls"
    override val baseUrl = "https://gist.github.com/"

    override val availableFilters: Map<String, List<String>> = mapOf(
        "style" to listOf("all", "light", "dark")
    )

    private val api = RetrofitHelper.create<OWalls>(baseUrl)
    private val resultsPerPage = 20

    private var wallpapers: MutableList<Wallpaper> = mutableListOf()
    private val filteredWallpapers: MutableList<Wallpaper>
        get() = when (val query = selectedFilters["style"]) {
            "light" -> wallpapers.filter { it.category == query }
            "dark" -> wallpapers.filter { it.category == query }
            else -> wallpapers
        }.toMutableList()

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        if (wallpapers.isEmpty()) loadAll()

        if (resultsPerPage * (page - 1) > filteredWallpapers.size) {
            return emptyList()
        }

        return filteredWallpapers.toList().subList(
            (page - 1) * resultsPerPage,
            minOf(page * resultsPerPage, filteredWallpapers.size)
        )
    }

    private suspend fun loadAll() {
        api.getAll().forEach { (_, images) ->
            images.jsonObject.forEach { (category, source) ->
                val imgSrc = source.jsonPrimitive.content
                if (imgSrc.isNotBlank()) {
                    wallpapers.add(
                        0,
                        Wallpaper(imgSrc = imgSrc, category = category)
                    )
                }
            }
        }
    }

    override suspend fun getRandomWallpaperUrl(): String? {
        if (wallpapers.isEmpty()) loadAll()
        return filteredWallpapers.randomOrNull()?.imgSrc
    }
}
