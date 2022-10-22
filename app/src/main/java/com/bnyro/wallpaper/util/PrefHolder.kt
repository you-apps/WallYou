package com.bnyro.wallpaper.util

import android.content.Context
import android.content.SharedPreferences

object PrefHolder {
    const val cropImagesKey = "cropImages"
    const val diskCacheKey = "diskCache"
    const val wallpaperChangerKey = "wallpaperChanger"
    const val wallpaperChangerIntervalKey = "wallpaperChangerInterval"
    const val wallpaperChangerTargetKey = "wallpaperChangerTarget"

    private const val prefFile = "preferences"
    lateinit var Preferences: SharedPreferences
    val PrefEditor: SharedPreferences.Editor
        get() = Preferences.edit()

    fun init(context: Context) {
        Preferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE)
    }
}
