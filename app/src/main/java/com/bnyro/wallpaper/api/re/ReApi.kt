package com.bnyro.wallpaper.api.re

import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitBuilder

class ReApi : Api() {
    override val name = "Reddit"
    override val baseUrl = "https://www.reddit.com/"
    override val filters: Map<String, List<String>> = mapOf()
    override val supportsTags: Boolean = false

    val api = RetrofitBuilder.create(baseUrl, Reddit::class.java)

    private val subreddit = "wallpaper"
    private val sort = "top"
    private val time = "week"
    private val imageRegex = Regex("^.+\\.(jpg|jpeg|png|webp)\$")

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        if (page != 1) return emptyList() // TODO: Pagination

        return api.getRedditData(subreddit, sort, time).data?.children?.filter {
            it.childdata.url?.matches(imageRegex) == true
        }?.map {
            with(it.childdata) {
                Wallpaper(
                    preview?.images?.firstOrNull()?.source?.imgUrl ?: url!!,
                    it.childdata.title,
                    thumb = url,
                    resolution = preview?.images?.firstOrNull()?.source?.let { img -> "${img.width}x${img.height}" }
                )
            }
        }.orEmpty()
    }

    override suspend fun getRandomWallpaperUrl(): String? = getWallpapers(1).randomOrNull()?.url
}
