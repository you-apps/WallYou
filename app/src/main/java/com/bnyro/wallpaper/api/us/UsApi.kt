package com.bnyro.wallpaper.api.us

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitHelper

class UsApi : Api() {
    override val name: String = "Unsplash"
    override val baseUrl: String = "https://unsplash.com"
    override val icon = Icons.Default.WaterDrop

    override val filters: Map<String, List<String>> = mapOf(
        "orientation" to listOf("any", "landscape", "portrait", "squarish"),
       "order_by" to listOf("latest", "oldest", "popular")
    )
    override val supportsTags: Boolean = true

    private val api = RetrofitHelper.create<Unsplash>(baseUrl)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        val tags = getTags()
        val sortOrder = getQuery("order_by")
        val orientation = getQuery("orientation").takeIf { it != "any" }

        val wallpapers = if (tags.isEmpty()) {
            api.getWallpapers(page, orientation, sortOrder)
        } else {
            val tagString = tags.joinToString(" ")
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
        val tagString = getTags().joinToString(" ")

        return api.getRandom(tagString).let {
            it.links.download ?: it.urls?.raw ?: it.urls?.full
        }
    }
}
