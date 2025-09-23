package com.bnyro.wallpaper.api.pb

import com.bnyro.wallpaper.api.pb.obj.PixabayResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Pixabay {
    @GET("{category}/search/{query}/")
    @Headers(
        "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:143.0) Gecko/20100101 Firefox/143.0 Pixabay",
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