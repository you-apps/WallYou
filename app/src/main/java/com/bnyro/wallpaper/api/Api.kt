package com.bnyro.wallpaper.api

import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.Preferences

abstract class Api {
    abstract val name: String
    abstract val baseUrl: String
    abstract val filters: Map<String, List<String>>
    abstract val supportsTags: Boolean

    abstract suspend fun getWallpapers(page: Int): List<Wallpaper>

    abstract suspend fun getRandomWallpaperUrl(): String?

    fun getPref(key: String, defValue: String): String {
        return Preferences.getString(this.name + key, defValue) ?: defValue
    }

    fun setPref(key: String, value: String) {
        Preferences.edit { putString(this@Api.name + key, value) }
    }

    fun getQuery(key: String): String {
        return getPref(key, filters[key]?.firstOrNull() ?: "")
    }

    fun getTags(): List<String> {
        return getPref(this.name + "tags", "sunset,portrait,display").split(",").filter {
            it.isNotBlank()
        }
    }

    fun setTags(tags: List<String>) {
        setPref(this.name + "tags", tags.joinToString(","))
    }
}
