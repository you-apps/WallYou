package net.youapps.wallpaper_apis.re

import com.fleeksoft.ksoup.Ksoup
import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper
import net.youapps.wallpaper_apis.WallpaperApi
import net.youapps.wallpaper_apis.re.obj.RedditGalleryItem
import net.youapps.wallpaper_apis.re.obj.RedditListingResponse
import java.time.Instant

class RedditApi : WallpaperApi() {
    override val name = "Reddit"
    override val baseUrl = "https://www.reddit.com/"
    override val requiresCommunityName: Boolean = true

    override val availableFilters: Map<String, List<String>> = mapOf(
        "sort" to listOf("hot", "top", "new", "rising"),
        "time" to listOf("day", "week", "month", "year", "all", "hour")
    )

    val api = RetrofitHelper.create<Reddit>(baseUrl)

    override var communityName: String? = "r/wallpaper"

    /**
     * Optional provider for session cookies (e.g. from an in-app browser session)
     * to avoid Reddit HTTP 429 rate limits and login walls.
     */
    var cookieProvider: (() -> String?)? = null

    private val imageRegex = Regex("^.+\\.(jpg|jpeg|png|webp)(\\?.*)?$", RegexOption.IGNORE_CASE)

    private var nextPageAfter: String? = null
    private var isUsingRssFallback = false

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        // happens when there's no next page available
        if (page != 1 && nextPageAfter == null) return emptyList()

        // reset the after query if starting from the beginning
        if (page == 1) {
            nextPageAfter = null
            isUsingRssFallback = false
        }

        val subreddit = communityName.orEmpty().trim().removePrefix("r/").removePrefix("/")
        if (subreddit.isEmpty()) return emptyList()

        val sort = selectedFilters["sort"] ?: "hot"
        val time = selectedFilters["time"]
        val cookie = cookieProvider?.invoke()

        // 1. Primary Strategy: Fetch rich JSON listing
        if (!isUsingRssFallback) {
            try {
                val response = api.getRedditJson(
                    subreddit = subreddit,
                    sort = sort,
                    limit = 25,
                    time = time,
                    after = nextPageAfter,
                    cookie = cookie
                )

                if (response.isSuccessful) {
                    val listing = response.body()
                    nextPageAfter = listing?.data?.after
                    val wallpapers = parseJsonListing(listing)
                    if (wallpapers.isNotEmpty() || listing?.data?.children?.isNotEmpty() == true) {
                        return wallpapers
                    }
                } else if (response.code() in listOf(302, 403, 429)) {
                    isUsingRssFallback = true
                }
            } catch (_: Exception) {
                isUsingRssFallback = true
            }
        }

        // 2. Fallback Strategy: Scrape RSS feed
        return fetchFromRss(subreddit, sort, time, cookie, page)
    }

    private fun parseJsonListing(listing: RedditListingResponse?): List<Wallpaper> {
        val children = listing?.data?.children?.mapNotNull { it.data } ?: return emptyList()
        val result = mutableListOf<Wallpaper>()

        for (post in children) {
            if (post.id.isEmpty()) continue

            val createdDate = formatDate(post.createdUtc)
            val postUrl = if (!post.permalink.isNullOrEmpty()) "https://www.reddit.com${post.permalink}" else null

            // Handle gallery posts
            val mediaMetadata = post.mediaMetadata
            val galleryItems = post.galleryData?.items
            if (post.isGallery && !mediaMetadata.isNullOrEmpty()) {
                val itemsToProcess = if (!galleryItems.isNullOrEmpty()) {
                    galleryItems.mapNotNull { item ->
                        mediaMetadata[item.mediaId]?.let { item to it }
                    }
                } else {
                    mediaMetadata.entries.map { (id, item) -> RedditGalleryItem(mediaId = id) to item }
                }

                val total = itemsToProcess.size
                itemsToProcess.forEachIndexed { index, (galleryItem, mediaItem) ->
                    if (mediaItem.status == "valid" || mediaItem.status == null) {
                        val rawUrl = mediaItem.s?.u ?: mediaItem.s?.gif
                        if (rawUrl != null) {
                            val fullImg = unescapeUrl(rawUrl)
                            val thumbUrl = mediaItem.p.lastOrNull()?.u?.let { unescapeUrl(it) } ?: fullImg
                            val source = mediaItem.s
                            val res = if (source != null && source.x > 0 && source.y > 0) "${source.x}x${source.y}" else null
                            val titleText = if (total > 1) "${post.title} (${index + 1}/$total)" else post.title

                            result.add(
                                Wallpaper(
                                    imgSrc = fullImg,
                                    title = titleText,
                                    thumb = thumbUrl,
                                    url = postUrl,
                                    author = post.author,
                                    resolution = res,
                                    creationDate = createdDate
                                )
                            )
                        }
                    }
                }
                continue
            }

            // Handle single image posts
            var imgSrc: String? = null
            var thumb: String? = null
            var resolution: String? = null

            val previewImage = post.preview?.images?.firstOrNull()
            if (previewImage?.source != null && previewImage.source.url.isNotEmpty()) {
                imgSrc = unescapeUrl(previewImage.source.url)
                resolution = if (previewImage.source.width > 0 && previewImage.source.height > 0) {
                    "${previewImage.source.width}x${previewImage.source.height}"
                } else null
                thumb = previewImage.resolutions.lastOrNull()?.url?.let { unescapeUrl(it) }
            }

            val url = post.url
            if (url.endsWith(".jpg", true) || url.endsWith(".png", true) ||
                url.endsWith(".jpeg", true) || url.endsWith(".webp", true) ||
                url.contains("i.redd.it")
            ) {
                imgSrc = url
            }

            if (thumb == null) {
                thumb = post.thumbnail?.takeIf { it.startsWith("http") } ?: imgSrc
            }

            if (imgSrc != null) {
                result.add(
                    Wallpaper(
                        imgSrc = imgSrc,
                        title = post.title,
                        thumb = thumb,
                        url = postUrl,
                        author = post.author,
                        resolution = resolution,
                        creationDate = createdDate
                    )
                )
            }
        }

        return result
    }

    private suspend fun fetchFromRss(
        subreddit: String,
        sort: String,
        time: String?,
        cookie: String?,
        page: Int
    ): List<Wallpaper> {
        return try {
            val xml = api.getRedditData(
                subreddit = subreddit,
                sort = sort,
                time = time,
                after = nextPageAfter,
                cookie = cookie
            ).string()

            val doc = Ksoup.parseXml(xml)
            val entries = doc.select("entry")

            nextPageAfter = entries.lastOrNull()?.selectFirst("id")?.text()

            entries.mapNotNull { entry ->
                val contentHtml = entry.selectFirst("content")?.text().orEmpty()
                val content = Ksoup.parse(contentHtml)

                val imgSrc = content.select("a[href]")
                    .map { it.attr("href") }
                    .firstOrNull { it.matches(imageRegex) || it.contains("i.redd.it") }
                    ?: return@mapNotNull null

                val unescapedImgSrc = unescapeUrl(imgSrc)
                val thumb = content.selectFirst("img")?.attr("src")?.let { unescapeUrl(it) }

                Wallpaper(
                    imgSrc = unescapedImgSrc,
                    title = entry.selectFirst("title")?.text(),
                    thumb = thumb ?: unescapedImgSrc,
                    url = entry.selectFirst("link")?.attr("href"),
                    author = entry.selectFirst("author")?.selectFirst("name")?.text(),
                    creationDate = entry.selectFirst("published")?.text()?.take(10)
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun unescapeUrl(url: String): String =
        url.replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")

    private fun formatDate(epochSeconds: Double): String? {
        if (epochSeconds <= 0.0) return null
        return try {
            Instant.ofEpochSecond(epochSeconds.toLong()).toString().take(10)
        } catch (_: Throwable) {
            null
        }
    }

    override suspend fun getRandomWallpaperUrl(): String? = getWallpapers(1).randomOrNull()?.imgSrc
}
