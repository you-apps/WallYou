package net.youapps.wallpaper_apis.us

import net.youapps.wallpaper_apis.us.obj.UsImage
import net.youapps.wallpaper_apis.us.obj.UsSearch
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

// The headers set below are required to bypass the Anubis bot detection
// that intercepts each request we send to Unsplash's API
private const val USER_AGENT = "wallpaper-apis/1.0.0"

interface Unsplash {
    @Headers("User-Agent: $USER_AGENT", "Accept: */*")
    @GET("/napi/photos")
    suspend fun getWallpapers(
        @Query("page") page: Int,
        @Query("orientation") orientation: String?,
        @Query("order_by") order: String
    ): List<UsImage>

    @Headers("User-Agent: $USER_AGENT", "Accept: */*")
    @GET("/napi/search/photos")
    suspend fun searchWallpapers(
        @Query("page") page: Int,
        @Query("query") query: String,
        @Query("orientation") orientation: String?,
        @Query("order_by") order: String
    ): UsSearch

    @Headers("User-Agent: $USER_AGENT", "Accept: */*")
    @GET("/napi/photos/random")
    suspend fun getRandom(
        @Query("query") query: String
    ): UsImage
}
