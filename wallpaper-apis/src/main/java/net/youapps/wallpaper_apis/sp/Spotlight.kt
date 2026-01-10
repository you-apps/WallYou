package net.youapps.wallpaper_apis.sp

import net.youapps.wallpaper_apis.sp.obj.SpotlightImage
import net.youapps.wallpaper_apis.sp.obj.SpotlightPage
import retrofit2.http.GET
import retrofit2.http.Query

private const val PLACEMENT_ID = "88000820"

interface Spotlight {
    @GET("v4/api/selection")
    suspend fun getLatestPage(
        @Query("country") country: String = "US",
        @Query("locale") locale: String = "en-US",
        @Query("fmt") format: String = "json",
        @Query("placement") placement: String = PLACEMENT_ID,
        @Query("bcnt") limit: Int = 4
    ): SpotlightPage

    @GET("v4/api/selection")
    suspend fun getLatest(
        @Query("country") country: String = "US",
        @Query("locale") locale: String = "en-US",
        @Query("fmt") format: String = "json",
        @Query("placement") placement: String = PLACEMENT_ID
    ): SpotlightImage
}