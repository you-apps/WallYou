package com.bnyro.wallpaper.api.px

import com.bnyro.wallpaper.api.px.obj.PixelWallsResponse
import retrofit2.http.GET

interface Pixel {
    @GET("/repos/wacko1805/Pixel-Wallpapers/git/trees/main?recursive=1")
    suspend fun getWallpapers(): PixelWallsResponse
}