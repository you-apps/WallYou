package com.bnyro.wallpaper.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.bnyro.wallpaper.App
import com.bnyro.wallpaper.obj.WallpaperConfig
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

object Preferences {
    const val resizeMethodKey = "resizeMethod"
    const val diskCacheKey = "diskCache"
    const val themeModeKey = "themeModeKey"
    const val wallpaperHistory = "autoAddToFavorites"
    const val grayscaleKey = "grayscale"
    const val blurKey = "blur"
    const val startTabKey = "startTab"
    const val invertBitmapBySystemThemeKey = "invertBitmapBySystemTheme"
    const val contrastKey = "contrast"

    const val wallpaperChangerKey = "wallpaperChanger"
    private const val wallpaperChangerConfigKey = "wallpaperChangerConfigurations"

    const val defaultDiskCacheSize = 128L * 1024 * 1024
    const val defaultWallpaperChangeInterval = 12L * 60

    private const val prefFile = "preferences"
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE)
    }

    fun getBoolean(key: String, defValue: Boolean) = preferences.getBoolean(key, defValue)
    fun getString(key: String, defValue: String) = preferences.getString(key, defValue) ?: defValue

    fun getFloat(key: String, defValue: Float) = preferences.getFloat(key, defValue)

    fun edit(action: SharedPreferences.Editor.() -> Unit) {
        preferences.edit().apply(action).apply()
    }

    fun getApiByRoute(route: String) = App.apis.firstOrNull { it.route == route } ?: App.apis.first()

    fun setWallpaperConfigs(configs: List<WallpaperConfig>) {
        edit { putString(wallpaperChangerConfigKey, RetrofitHelper.json.encodeToString(configs)) }
    }

    fun getWallpaperConfigs(): List<WallpaperConfig> {
        val prefString = getString(wallpaperChangerConfigKey, "")

        return try {
            if (prefString.isEmpty()) throw IllegalArgumentException("No saved wallpaper config yet.")

            RetrofitHelper.json.decodeFromString<List<WallpaperConfig>>(prefString)
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.stackTraceToString())
            listOf()
        }
    }
}
