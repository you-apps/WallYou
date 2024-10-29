package com.bnyro.wallpaper.api.re

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import com.bnyro.wallpaper.api.CommunityApi
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitHelper

class ReApi : CommunityApi() {
    override val name = "Reddit"
    override val baseUrl = "https://www.reddit.com/"
    override val icon = Icons.Default.Forum

    override val filters: Map<String, List<String>> = mapOf(
        "sort" to listOf("top", "new", "hot", "rising"),
        "time" to listOf("month", "year", "hour", "day", "week")
    )

    val api = RetrofitHelper.create(baseUrl, Reddit::class.java)

    override val defaultCommunityName = "r/wallpaper"

    private val imageRegex = Regex("^.+\\.(jpg|jpeg|png|webp)\$")

    private var nextPageAfter: String? = null

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        // happens when there's no next page available
        if (page != 1 && nextPageAfter == null) return emptyList()

        // reset the after query if starting from the beginning
        if (page == 1) nextPageAfter = null
        val communityName = getCommunityName().replaceFirst("r/", "")

        val response = api.getRedditData(communityName, getQuery("sort"), getQuery("time"), nextPageAfter)
        // needed in order to load the next page
        nextPageAfter = response.data?.after

        return response.data?.children?.filter {
            it.childData.url?.matches(imageRegex) == true
        }?.map {
            with(it.childData) {
                Wallpaper(
                    imgSrc = preview?.images?.firstOrNull()?.source?.imgUrl ?: url!!,
                    title = it.childData.title,
                    thumb = url,
                    resolution = preview?.images?.firstOrNull()?.source?.let { img -> "${img.width}x${img.height}" }
                )
            }
        }.orEmpty()
    }

    override suspend fun getRandomWallpaperUrl(): String? = getWallpapers(1).randomOrNull()?.imgSrc
}
