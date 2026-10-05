package net.youapps.wallpaper_apis.na

import net.youapps.wallpaper_apis.WallpaperApi
import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper

class NasaPotdApi : WallpaperApi() {
    override val name: String = "NASA APOD"
    override val baseUrl: String = "https://science.nasa.gov/"

    override val availableFilters: Map<String, List<String>> = mapOf()
        // get() = mapOf("order" to listOf("date", "random"))

    private val api = RetrofitHelper.create<NasaAPOD>(baseUrl)

    // private var nextEndDate: LocalDateTime? = null

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
//        val sortByDate = selectedFilters["order"] == "date"
//
//        val response = if (sortByDate) {
//            val endDate = if (page == 1 || nextEndDate == null) {
//                LocalDateTime.now()
//            } else {
//                nextEndDate
//            }
//            nextEndDate = endDate!!.minusDays(10)
//
//            api.getImages(
//                endDate = endDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
//                startDate = nextEndDate!!.format(
//                    DateTimeFormatter.ISO_LOCAL_DATE
//                )
//            )
//        } else {
//            api.getImages(count = 10)
//        }

        // doesn't support pagination because their API is currently
        // broken
        if (page > 1) return emptyList() // https://github.com/nasa/apod-api/issues/182

        val response = api.getImages()

        return response
            .filter { it.mediaType == "image" }
            .map {
                Wallpaper(
                    imgSrc = it.hdUrl,
                    author = it.copyright,
                    creationDate = it.date,
                    title = it.title,
                    description = it.alt
                )
            }
    }

    override suspend fun getRandomWallpaperUrl(): String {
        return api.getImages().first().hdUrl
    }
}