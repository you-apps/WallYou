package com.bnyro.wallpaper.api.wh

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Landscape
import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitHelper

class WhApi : Api() {
    override val name: String = "Wallhaven"
    override val baseUrl: String = "https://wallhaven.cc/api/v1/"
    override val icon = Icons.Default.Landscape

    override val filters: Map<String, List<String>> = mapOf(
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
            query = getTags().joinToString(" "),
            ratios = getQuery("ratios"),
            categories = when (getQuery("categories")) {
                "general" -> "100"
                "anime" -> "010"
                "people" -> "001"
                else -> "111"
            },
            sorting = getQuery("sorting"),
            order = getQuery("order"),
            purity = when (getQuery("purity")) {
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
