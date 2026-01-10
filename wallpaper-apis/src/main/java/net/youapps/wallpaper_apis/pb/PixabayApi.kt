package net.youapps.wallpaper_apis.pb

import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper
import net.youapps.wallpaper_apis.WallpaperApi

class PixabayApi: WallpaperApi() {
    override val name: String = "Pixabay"
    override val baseUrl: String = "https://pixabay.com"

    override val availableFilters: Map<String, List<String>> = mapOf(
        "category" to listOf("photos", "illustrations", "vectors"),
        "orientation" to listOf("all", "vertical", "horizontal"),
        "content_type" to listOf("all", "authentic", "ai"),
        "colors" to listOf("all", "red", "green", "blue", "yellow", "turquoise", "lilac", "pink", "white", "gray", "black", "brown")
    )
    override val supportsTags: Boolean = true

    private val api = RetrofitHelper.create<Pixabay>(baseUrl)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        return api.getWallpapers(
            query = selectedTags.joinToString(" "),
            category = selectedFilters["category"]!!,
            pageNumber = page,
            orientation = selectedFilters["orientation"].takeIf { it != "all" },
            contentType = selectedFilters["content_type"].takeIf { it != "all" },
            colors = selectedFilters["colors"].takeIf { it != "all" },
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