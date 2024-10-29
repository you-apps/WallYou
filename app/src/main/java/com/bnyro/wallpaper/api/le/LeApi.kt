package com.bnyro.wallpaper.api.le

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import com.bnyro.wallpaper.api.CommunityApi
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitHelper
import com.bnyro.wallpaper.util.TextUtils

class LeApi : CommunityApi() {
    override val name = "Lemmy"
    override val baseUrl = "https://lemmy.ml"
    override val icon = Icons.Default.Book

    override val filters: Map<String, List<String>> = mapOf(
        "sort" to listOf(
            "Active",
            "Hot",
            "New",
            "Old",
            "TopDay",
            "TopWeek",
            "TopMonth",
            "TopYear",
            "TopAll",
            "MostComments",
            "NewComments",
            "TopHour",
            "TopSixHour",
            "TopTwelveHour",
            "TopThreeMonths",
            "TopSixMonths",
            "TopNineMonths"
        ),
        "type" to listOf("All", "Local")
    )

    override val defaultCommunityName: String = "pics@lemmy.world"

    private val api = RetrofitHelper.create(baseUrl, Lemmy::class.java)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        return api.getWallpapers(
            page = page,
            communityName = getCommunityName(),
            sort = getQuery("sort"),
            type = getQuery("type")
        ).posts.filter {
            !it.post.thumbnailUrl.isNullOrEmpty()
        }.map {
            Wallpaper(
                imgSrc = it.post.thumbnailUrl!!,
                thumb = "${it.post.thumbnailUrl}?format=jpg&thumbnail=1080",
                title = it.post.name,
                description = it.post.body?.let { text -> TextUtils.removeMarkdownSymbols(text) },
                url = it.post.postUrl,
                author = it.creator.name,
                creationDate = it.post.published
            )
        }
    }

    override suspend fun getRandomWallpaperUrl(): String? = getWallpapers(1).randomOrNull()?.imgSrc
}
