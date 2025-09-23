package com.bnyro.wallpaper.api.le

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import com.bnyro.wallpaper.api.CommunityApi
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitHelper
import com.bnyro.wallpaper.util.TextUtils
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

class LemmyApi : CommunityApi() {
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

    private val api = RetrofitHelper.create<Lemmy>(baseUrl)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        return api.getWallpapers(
            page = page,
            communityName = getCommunityName(),
            sort = getQuery("sort"),
            type = getQuery("type")
        ).posts.filter {
            !it.post.thumbnailUrl.isNullOrEmpty()
        }.map {
            val imgUrl = extractImageUrl(it.post.thumbnailUrl!!)

            Wallpaper(
                imgSrc = imgUrl,
                thumb = "$imgUrl?format=jpg&thumbnail=1080",
                title = it.post.name,
                description = it.post.body?.let { text -> TextUtils.removeMarkdownSymbols(text) },
                url = it.post.postUrl,
                author = it.creator.name,
                creationDate = it.post.published
            )
        }
    }

    private fun extractImageUrl(plainUrl: String): String {
        val url = plainUrl.toHttpUrlOrNull() ?: return plainUrl

        // proxied urls: https://lemmy.local/api/v3/image_proxy?url=...
        if (!url.queryParameter("url").isNullOrEmpty()) {
            return url.queryParameter("url")!!
        }

        return plainUrl
    }

    override suspend fun getRandomWallpaperUrl(): String? = getWallpapers(1).randomOrNull()?.imgSrc
}
