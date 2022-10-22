package com.bnyro.wallpaper.api

import com.bnyro.wallpaper.obj.Wallpaper
import com.bnyro.wallpaper.util.PrefHolder

abstract class Api {
    abstract val name: String
    abstract val baseUrl: String
    abstract suspend fun getWallpapers(page: Int): List<Wallpaper>

    fun getPref(key: String, defValue: String): String {
        return PrefHolder.Preferences.getString(this.name + key, defValue) ?: defValue
    }

    fun setPref(key: String, value: String) {
        PrefHolder.PrefEditor.putString(this.name + key, value).apply()
    }
}
