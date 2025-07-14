package com.bnyro.wallpaper.api.ze


import com.bnyro.wallpaper.api.ze.obj.ZedgeRequest
import com.bnyro.wallpaper.api.ze.obj.ZedgeResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface Zedge {
    @POST("api/graphql")
    suspend fun getWallpapers(@Body body: ZedgeRequest): ZedgeResponse
}