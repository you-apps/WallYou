package com.bnyro.wallpaper.util

import android.content.Context
import android.content.SharedPreferences

object PrefHolder {
    const val cropImagesKey = "cropImages"

    private const val prefFile = "preferences"
    lateinit var Preferences: SharedPreferences
    val PrefEditor: SharedPreferences.Editor
        get() = Preferences.edit()

    fun init(context: Context) {
        Preferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE)
    }
}
