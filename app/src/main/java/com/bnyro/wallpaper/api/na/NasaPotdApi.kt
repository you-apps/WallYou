package com.bnyro.wallpaper.api.na

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitHelper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NasaPotdApi : Api() {
    override val name: String = "NASA APOD"
    override val icon: ImageVector = Icons.Default.Star
    override val baseUrl: String = "https://api.nasa.gov/"

    override val filters: Map<String, List<String>>
        get() = mapOf("order" to listOf("date", "random"))

    private val api = RetrofitHelper.create<NasaAPOD>(baseUrl)

    private var nextEndDate: LocalDateTime? = null

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        val sortByDate = getQuery("order") == "date"

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