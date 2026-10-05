package net.youapps.wallpaper_apis.na

import net.youapps.wallpaper_apis.na.obj.NasaImage
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "DEMO_KEY"

interface NasaAPOD {
    @GET("wp-json/wp/v2/apod-basic")
    suspend fun getImages(
        @Query("api_key") apiKey: String = API_KEY,
    ): List<NasaImage>
}