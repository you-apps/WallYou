package net.youapps.wallpaper_apis.na

import net.youapps.wallpaper_apis.na.obj.NasaImage
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "DEMO_KEY"

interface NasaAPOD {
    @GET("planetary/apod")
    suspend fun getImages(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("count") count: Int? = null,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null
    ): List<NasaImage>

    @GET("planetary/apod")
    suspend fun getLatestImage(
        @Query("api_key") apiKey: String = API_KEY,
    ): NasaImage
}