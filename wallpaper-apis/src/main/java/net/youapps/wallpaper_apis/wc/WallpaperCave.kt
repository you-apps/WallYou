package net.youapps.wallpaper_apis.wc

import net.youapps.wallpaper_apis.wc.obj.WallpaperCaveMoreResponse
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface WallpaperCave {
    @POST("/morefeatured")
    @Multipart
    suspend fun getFeaturedWallpapers(
        @Part("page") page: Int
    ): List<WallpaperCaveMoreResponse>

    @POST("/morelatest")
    @Multipart
    suspend fun getLatestWallpapers(
        @Part("page") page: Int
    ): List<WallpaperCaveMoreResponse>
}