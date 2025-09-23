package com.bnyro.wallpaper.api.pb

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.ui.graphics.vector.ImageVector
import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitHelper

class PixabayApi: Api() {
    override val name: String = "Pixabay"
    override val icon: ImageVector = Icons.Default.Palette
    override val baseUrl: String = "https://pixabay.com"

    override val filters: Map<String, List<String>> = mapOf(
        "category" to listOf("photos", "illustrations", "vectors"),
        "orientation" to listOf("all", "vertical", "horizontal"),
        "content_type" to listOf("all", "authentic", "ai"),
        "colors" to listOf("all", "red", "green", "blue", "yellow", "turquoise", "lilac", "pink", "white", "gray", "black", "brown")
    )
    override val supportsTags: Boolean = true

    private val api = RetrofitHelper.create<Pixabay>(baseUrl)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        return api.getWallpapers(
            query = getTags().joinToString(" "),
            category = getQuery("category"),
            pageNumber = page,
            orientation = getQuery("orientation").takeIf { it != "all" },
            contentType = getQuery("content_type").takeIf { it != "all" },
            colors = getQuery("colors").takeIf { it != "all" },
        ).page.results.map { result ->
            Wallpaper(
                url = baseUrl + result.href,
                imgSrc = result.sources.values.last(),
                thumb = result.sources.values.first(),
                title = result.alt,
                author = result.user.username,
                description = result.description,
                category = result.name,
                resolution = "${result.width}x${result.height}",
                creationDate = result.uploadDate
            )
        }
    }

    override suspend fun getRandomWallpaperUrl(): String? = getWallpapers(1).randomOrNull()?.imgSrc
}