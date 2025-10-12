package com.bnyro.wallpaper.api.ze


import com.bnyro.wallpaper.api.ze.obj.ZedgeRequest
import com.bnyro.wallpaper.api.ze.obj.ZedgeResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface Zedge {
    @POST("api/graphql")
    suspend fun getWallpapers(@Body body: ZedgeRequest): ZedgeResponse

    @GET("wallpapers/{id}")
    suspend fun getWallpaperSource(
        @Path("id") id: String,
        @Header("RSC") rsc: Int = 1
    ): ResponseBody
}