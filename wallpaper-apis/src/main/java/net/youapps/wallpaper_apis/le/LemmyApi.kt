package net.youapps.wallpaper_apis.le

import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.TextUtils
import net.youapps.wallpaper_apis.Wallpaper
import net.youapps.wallpaper_apis.WallpaperApi
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

class LemmyApi : WallpaperApi() {
    override val name = "Lemmy"
    override val baseUrl = "https://lemmy.ml"
    override val requiresCommunityName = true

    override val availableFilters: Map<String, List<String>> = mapOf(
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

    override var communityName: String? = "pics@lemmy.world"

    private val api = RetrofitHelper.create<Lemmy>(baseUrl)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        return api.getWallpapers(
            page = page,
            communityName = communityName ?: throw IllegalArgumentException("no community name specified"),
            sort = selectedFilters["sort"]!!,
            type = selectedFilters["type"]!!
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
