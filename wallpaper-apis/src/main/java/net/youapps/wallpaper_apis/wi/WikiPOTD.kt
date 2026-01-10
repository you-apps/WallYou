package net.youapps.wallpaper_apis.wi

import net.youapps.wallpaper_apis.wi.obj.WikiImagesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WikiPOTD {
    @GET("w/api.php")
    suspend fun getImages(
        @Query("action") action: String = "query",
        @Query("format") format: String = "json",
        @Query("generator") generator: String = "images",
        @Query("prop") prop: String = "imageinfo",
        @Query("iiprop") iiProp: String = "url|size",
        @Query("iiurlwidth") iiUrlWidth: String = "300",
        @Query("titles") titles: String, // separated by "|"
    ): WikiImagesResponse
}