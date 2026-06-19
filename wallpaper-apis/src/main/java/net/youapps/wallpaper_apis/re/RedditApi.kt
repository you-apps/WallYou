package net.youapps.wallpaper_apis.re

import com.fleeksoft.ksoup.Ksoup
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
        val subreddit = communityName!!.replaceFirst("r/", "")

        val xml = api.getRedditData(
            subreddit,
            selectedFilters["sort"]!!,
            selectedFilters["time"],
            nextPageAfter
        ).string()

        val doc = Ksoup.parseXml(xml)
        val entries = doc.select("entry")

        nextPageAfter = entries.lastOrNull()?.selectFirst("id")?.text()

        return entries.mapNotNull { entry ->
            val content = Ksoup.parse(entry.selectFirst("content")?.text().orEmpty())
            val imgSrc = content.select("a[href]")
                .map { it.attr("href") }
                .firstOrNull { it.matches(imageRegex) } ?: return@mapNotNull null

            Wallpaper(
                imgSrc = imgSrc,
                title = entry.selectFirst("title")?.text(),
                thumb = content.selectFirst("img")?.attr("src"),
                url = entry.selectFirst("link")?.attr("href"),
                author = entry.selectFirst("author")?.selectFirst("name")?.text(),
                creationDate = entry.selectFirst("published")?.text()?.take(10),
            )
        }
    }

    override suspend fun getRandomWallpaperUrl(): String? = getWallpapers(1).randomOrNull()?.imgSrc
}
