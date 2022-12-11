package com.bnyro.wallpaper.api.ps

import com.bnyro.wallpaper.api.ps.obj.PsImage
import retrofit2.http.GET
import retrofit2.http.Query

interface Picsum {
    @GET("v2/list")
    suspend fun getWallpapers(
        @Query("page") page: Int,
        @Query("limit") limit: Int? = null
    ): List<PsImage>
}
