package net.youapps.wallpaper_apis.re

import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper
import net.youapps.wallpaper_apis.WallpaperApi

class RedditApi : WallpaperApi() {
    override val name = "Reddit"
    override val baseUrl = "https://www.reddit.com/"
    override val requiresCommunityName: Boolean = true

    override val availableFilters: Map<String, List<String>> = mapOf(
        "sort" to listOf("top", "new", "hot", "rising"),
        "time" to listOf("month", "year", "hour", "day", "week")
    )

    val api = RetrofitHelper.create<Reddit>(baseUrl)

    override var communityName: String? = "r/wallpaper"

    private val imageRegex = Regex("^.+\\.(jpg|jpeg|png|webp)$")

    private var nextPageAfter: String? = null

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        // happens when there's no next page available
        if (page != 1 && nextPageAfter == null) return emptyList()

        // reset the after query if starting from the beginning
        if (page == 1) nextPageAfter = null
        val communityName = communityName!!.replaceFirst("r/", "")

        val response =
            api.getRedditData(
                communityName,
                selectedFilters["sort"]!!,
                selectedFilters["time"],
                nextPageAfter
            )
        // needed in order to load the next page
        nextPageAfter = response.data?.after

        return response.data?.children?.filter {
            it.childData.url?.matches(imageRegex) == true
        }?.map { child ->
            val data = child.childData
            val preview = data.preview?.images?.firstOrNull()
            val thumb = preview?.resolutions?.sortedBy { it.height }
                ?.firstOrNull { it.height != null && it.height > 400 }?.imgUrl ?: data.thumbnail

            Wallpaper(
                imgSrc = preview?.source?.imgUrl ?: data.url.orEmpty(),
                title = data.title,
                thumb = thumb,
                resolution = preview?.source?.let { img -> "${img.width}x${img.height}" }
            )
        }.orEmpty()
    }

    override suspend fun getRandomWallpaperUrl(): String? = getWallpapers(1).randomOrNull()?.imgSrc
}
