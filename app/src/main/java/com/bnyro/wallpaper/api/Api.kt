package com.bnyro.wallpaper.api

import com.bnyro.wallpaper.obj.Wallpaper

abstract class Api {
    abstract val baseUrl: String

    abstract suspend fun getWallpapers(): List<Wallpaper>
}
