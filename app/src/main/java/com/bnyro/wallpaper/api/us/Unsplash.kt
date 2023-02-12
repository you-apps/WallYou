package com.bnyro.wallpaper.api.us

import com.bnyro.wallpaper.api.us.obj.UsImage
import com.bnyro.wallpaper.api.us.obj.UsSearch
import retrofit2.http.GET
import retrofit2.http.Query

interface Unsplash {
    @GET("/napi/photos")
    suspend fun getWallpapers(
        @Query("page") page: Int,
        @Query("order_by") order: String
    ): List<UsImage>

    @GET("/napi/search/photos")
    suspend fun searchWallpapers(
        @Query("page") page: Int,
        @Query("query") query: String,
        @Query("order_by") order: String
    ): UsSearch

    @GET("/napi/photos/random")
    suspend fun getRandom(): UsImage
}
