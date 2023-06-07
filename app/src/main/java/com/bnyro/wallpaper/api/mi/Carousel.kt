package com.bnyro.wallpaper.api.mi

import com.bnyro.wallpaper.api.mi.obj.MiFetch
import retrofit2.http.GET
import retrofit2.http.Query

interface Carousel {
    @GET("lock/lock_view")
    suspend fun getWallpapers(
        @Query("_res") res: String = "hd100000",
        @Query("_eimi") userid: String = "wall_ly_100001",
        @Query("page_size") page_size: Int = 20,
        @Query("channel_id") channel_id: String?
    ): MiFetch
}
