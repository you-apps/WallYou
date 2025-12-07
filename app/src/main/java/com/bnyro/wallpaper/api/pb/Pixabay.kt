package com.bnyro.wallpaper.api.pb

import com.bnyro.wallpaper.api.pb.obj.PixabayResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Pixabay {
    @GET("{category}/search/{query}/")
    @Headers(
        "Accept: 'application/json",
        "x-bootstrap-cache-miss: 1",
        "x-fetch-bootstrap: 1"
    )
    suspend fun getWallpapers(
        @Path("query") query: String,
        @Path("category") category: String,
        @Query("pagi") pageNumber: Int,
        @Query("orientation") orientation: String?,
        @Query("content_type") contentType: String?,
        @Query("colors") colors: String?
    ): PixabayResponse
}