package net.youapps.wallpaper_apis.us

import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper
import net.youapps.wallpaper_apis.WallpaperApi

class UnsplashApi : WallpaperApi() {
    override val name: String = "Unsplash"
    override val baseUrl: String = "https://unsplash.com"

    override val availableFilters: Map<String, List<String>> = mapOf(
        "orientation" to listOf("any", "landscape", "portrait", "squarish"),
       "order_by" to listOf("latest", "oldest", "popular")
    )
    override val supportsTags: Boolean = true

    private val api = RetrofitHelper.create<Unsplash>(baseUrl)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        val sortOrder = selectedFilters["order_by"]!!
        val orientation = selectedFilters["orientation"].takeIf { it != "any" }

        val wallpapers = if (selectedTags.isEmpty()) {
            api.getWallpapers(page, orientation, sortOrder)
        } else {
            val tagString = selectedTags.joinToString(" ")
            api.searchWallpapers(page, tagString, orientation, sortOrder).results
        }

        return wallpapers.filter { it.premium != true }.map {
            Wallpaper(
                imgSrc = it.links.download ?: it.urls?.raw ?: "",
                description = it.alt_description,
                author = it.user?.username,
                url = it.links.html,
                resolution = "${it.width}x${it.height}",
                thumb = it.urls?.small ?: it.urls?.regular ?: it.urls?.full ?: it.urls?.raw,
                creationDate = it.created_at
            )
        }
    }

    override suspend fun getRandomWallpaperUrl(): String? {
        val tagString = selectedTags.joinToString(" ")

        return api.getRandom(tagString).let {
            it.links.download ?: it.urls?.raw ?: it.urls?.full
        }
    }
}
