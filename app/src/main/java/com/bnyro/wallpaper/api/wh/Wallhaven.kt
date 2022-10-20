package com.bnyro.wallpaper.api.wh

import com.bnyro.wallpaper.api.wh.obj.WhSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Wallhaven {
    @GET("search")
    suspend fun search(
        @Query("q") query: String? = null,
        @Query("ratio") ratio: String? = null,
        @Query("page") page: Int = 1
    ): WhSearchResponse
}
