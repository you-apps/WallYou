package com.bnyro.wallpaper.api

import com.bnyro.wallpaper.obj.Wallpaper
import com.bnyro.wallpaper.util.PrefHolder

abstract class Api {
    abstract val name: String
    abstract val baseUrl: String
    abstract val filters: Map<String, List<String>>
    abstract val supportsTags: Boolean

    abstract suspend fun getWallpapers(page: Int): List<Wallpaper>

    fun getPref(key: String, defValue: String): String {
        return PrefHolder.Preferences.getString(this.name + key, defValue) ?: defValue
    }

    fun setPref(key: String, value: String) {
        PrefHolder.PrefEditor.putString(this.name + key, value).apply()
    }

    fun getQuery(key: String): String {
        return getPref(key, filters[key]?.first() ?: "")
    }

    fun getTags(): List<String> {
        return getPref(this.name + "tags", "sunset,portrait,display").split(",")
    }

    fun setTags(tags: List<String>) {
        setPref(this.name + "tags", tags.joinToString(","))
    }
}
