package net.youapps.wallpaper_apis.wi

import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper
import net.youapps.wallpaper_apis.WallpaperApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WikipediaPotdApi : WallpaperApi() {
    override val name = "Wikipedia POTD"
    override val baseUrl = "https://en.wikipedia.org"

    private val api = RetrofitHelper.create<WikiPOTD>(baseUrl)
    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        val now = LocalDate.now()
        val dates = mutableListOf<String>()

        for (i in 0 until PAGE_SIZE) {
            val offset = (page - 1) * PAGE_SIZE + i
            val date = now.minusDays(offset.toLong())
            dates.add(date.format(dateFormat))
        }

        val titles = dates.map { POTD_ARTICLE_PREFIX + it }
        return api.getImages(titles = titles.joinToString("|")).query.pages.values
            .filter { it.imageInfo.isNotEmpty() }
            .map { image ->
                val imageInfo = image.imageInfo.first()

                Wallpaper(
                    imgSrc = imageInfo.url,
                    url = imageInfo.descriptionUrl,
                    title = image.title.substringAfter(":").substringBeforeLast("."),
                    resolution = "${imageInfo.width}x${imageInfo.height}",
                    thumb = imageInfo.thumbUrl
                )
            }
    }

    override suspend fun getRandomWallpaperUrl(): String? {
        val today = LocalDate.now().format(dateFormat)

        return api.getImages(titles = POTD_ARTICLE_PREFIX + today).query.pages.values.firstOrNull()
            ?.imageInfo?.firstOrNull()?.url
    }

    companion object {
        private const val POTD_ARTICLE_PREFIX = "Template:POTD/"
        private const val PAGE_SIZE = 10
    }
}