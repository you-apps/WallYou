package net.youapps.wallpaper_apis.im

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface Imgth {
    @GET("live")
    suspend fun getLive(): ResponseBody

    @GET("gallery/{category}/page/{page}")
    suspend fun getCategory(
        @Path("category", encoded = true) category: String,
        @Path("page") page: Int
    ): ResponseBody
}