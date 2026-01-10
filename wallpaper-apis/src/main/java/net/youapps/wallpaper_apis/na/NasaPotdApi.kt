package net.youapps.wallpaper_apis.na

import net.youapps.wallpaper_apis.WallpaperApi
import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NasaPotdApi : WallpaperApi() {
    override val name: String = "NASA APOD"
    override val baseUrl: String = "https://api.nasa.gov/"

    override val availableFilters: Map<String, List<String>>
        get() = mapOf("order" to listOf("date", "random"))

    private val api = RetrofitHelper.create<NasaAPOD>(baseUrl)

    private var nextEndDate: LocalDateTime? = null

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        val sortByDate = selectedFilters["order"] == "date"

        val response = if (sortByDate) {
            val endDate = if (page == 1 || nextEndDate == null) {
                LocalDateTime.now()
            } else {
                nextEndDate
            }
            nextEndDate = endDate!!.minusDays(10)

            api.getImages(
                endDate = endDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                startDate = nextEndDate!!.format(
                    DateTimeFormatter.ISO_LOCAL_DATE
                )
            )
        } else {
            api.getImages(count = 10)
        }

        return response
            .filter { it.mediaType == "image" && (it.url ?: it.hdUrl) != null }
            .map {
                Wallpaper(
                    imgSrc = it.hdUrl ?: it.url!!,
                    thumb = it.url,
                    author = it.copyright,
                    creationDate = it.date,
                    title = it.title,
                    description = it.explanation
                )
            }
    }

    override suspend fun getRandomWallpaperUrl(): String? {
        val image = api.getLatestImage()
        return image.hdUrl ?: image.url
    }
}