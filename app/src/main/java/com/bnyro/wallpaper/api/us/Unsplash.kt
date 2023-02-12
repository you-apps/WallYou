package com.bnyro.wallpaper.api.us

import com.bnyro.wallpaper.api.us.obj.UsImage
import retrofit2.http.GET
import retrofit2.http.Query

interface Unsplash {
    @GET("/napi/photos")
    suspend fun getWallpapers(
        @Query("page") page: Int
    ): List<UsImage>

    @GET("/napi/photos/random")
    suspend fun getRandom(): UsImage
}
