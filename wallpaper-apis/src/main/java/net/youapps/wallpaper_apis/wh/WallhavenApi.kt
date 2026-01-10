package net.youapps.wallpaper_apis.wh

import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper
import net.youapps.wallpaper_apis.WallpaperApi

class WallhavenApi : WallpaperApi() {
    override val name: String = "Wallhaven"
    override val baseUrl: String = "https://wallhaven.cc/api/v1/"

    override val availableFilters: Map<String, List<String>> = mapOf(
        "ratios" to listOf("portrait", "landscape"),
        "categories" to listOf("general", "anime", "people", "all"),
        "sorting" to listOf("date_added", "favorites", "relevance", "random", "views", "toplist"),
        "order" to listOf("desc", "asc"),
        "purity" to listOf("sfw", "sketchy", "all")
    )
    override val supportsTags: Boolean = true

    private val api = RetrofitHelper.create<Wallhaven>(baseUrl)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        return api.search(
            page = page,
            query = selectedTags.joinToString(" "),
            ratios = selectedFilters["ratios"]!!,
            categories = when (selectedFilters["categories"]) {
                "general" -> "100"
                "anime" -> "010"
                "people" -> "001"
                else -> "111"
            },
            sorting = selectedFilters["sorting"]!!,
            order = selectedFilters["order"]!!,
            purity = when (selectedFilters["purity"]) {
                "sfw" -> "100"
                "sketchy" -> "010"
                else -> "110"
            }
        ).data?.map {
            Wallpaper(
                imgSrc = it.path!!,
                title = it.category,
                url = it.url,
                category = it.category,
                fileSize = it.file_size,
                resolution = it.resolution,
                thumb = it.thumbs?.original,
                creationDate = it.created_at
            )
        }.orEmpty()
    }

    override suspend fun getRandomWallpaperUrl(): String? {
        return getWallpapers(1).randomOrNull()?.imgSrc
    }
}
