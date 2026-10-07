package net.youapps.wallpaper_apis.na

import net.youapps.wallpaper_apis.WallpaperApi
import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper

// https://api.nasa.gov/assets/files/apod/APOD-APIs-and-RSS-info.pdf
class NasaPotdApi : WallpaperApi() {
    override val name: String = "NASA APOD"
    override val baseUrl: String = "https://science.nasa.gov/"

    override val availableFilters: Map<String, List<String>> = mapOf()

    private val api = RetrofitHelper.create<NasaAPOD>(baseUrl)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        val response = api.getImages(page = page)

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