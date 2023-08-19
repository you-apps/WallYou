package com.bnyro.wallpaper.api.re

import com.bnyro.wallpaper.api.CommunityApi
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitBuilder

class ReApi : CommunityApi() {
    override val name = "Reddit"
    override val baseUrl = "https://www.reddit.com/"

    val api = RetrofitBuilder.create(baseUrl, Reddit::class.java)

    override val defaultCommunityName: String = "r/wallpaper"

    private val sort = "top" // TODO: move to filter dialog by overriding [tags]
    private val time = "week" // TODO: move to filter dialog by overriding [tags]
    private val imageRegex = Regex("^.+\\.(jpg|jpeg|png|webp)\$")

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        if (page != 1) return emptyList() // TODO: Pagination

        val communityName = getCommunityName().replaceFirst("r/", "")
        return api.getRedditData(communityName, sort, time).data?.children?.filter {
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
