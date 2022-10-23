package com.bnyro.wallpaper.api.wh

import com.bnyro.wallpaper.api.wh.obj.WhSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Wallhaven {
    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("categories") categories: String,
        @Query("order") order: String,
        @Query("purity") purity: String,
        @Query("sorting") sorting: String,
        @Query("ratios") ratios: String
    ): WhSearchResponse
}
