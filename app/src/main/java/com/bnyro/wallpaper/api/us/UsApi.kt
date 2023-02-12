package com.bnyro.wallpaper.api.us

import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitBuilder

class UsApi() : Api() {
    override val name: String = "Unsplash"
    override val baseUrl: String = "https://unsplash.com"
    override val filters: Map<String, List<String>> = mapOf(
        "order_by" to listOf("latest", "oldest", "popular")
    )
    override val supportsTags: Boolean = true

    private val api = RetrofitBuilder.create(baseUrl, Unsplash::class.java)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        val tags = getTags()

        val wallpapers = if (tags.isEmpty()) {
            api.getWallpapers(page, getQuery("order_by"))
        } else {
            api.searchWallpapers(page, tags.joinToString(" "), getQuery("order_by")).results
        }

        return wallpapers.filter { it.premium != true }.map {
            Wallpaper(
                imgSrc = it.links.download ?: it.urls?.raw ?: "",
                author = it.user?.username,
                url = it.links.html,
                resolution = "${it.width}x${it.height}",
                thumb = it.urls?.small ?: it.urls?.regular ?: it.urls?.full ?: it.urls?.raw,
                creationDate = it.created_at
            )
        }
    }

    override suspend fun getRandomWallpaperUrl(): String? {
        return api.getRandom().let {
            it.links.download ?: it.urls?.raw ?: it.urls?.full
        }
    }
}
