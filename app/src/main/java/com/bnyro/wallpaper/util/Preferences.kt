package com.bnyro.wallpaper.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.bnyro.wallpaper.api.bi.BiApi
import com.bnyro.wallpaper.api.mi.MiApi
import com.bnyro.wallpaper.api.ow.OwApi
import com.bnyro.wallpaper.api.ps.PsApi
import com.bnyro.wallpaper.api.us.UsApi
import com.bnyro.wallpaper.api.wh.WhApi
import com.bnyro.wallpaper.enums.WallpaperConfig
import com.bnyro.wallpaper.enums.WallpaperTarget
import com.bnyro.wallpaper.ui.nav.DrawerScreens
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

object Preferences {
    const val cropImagesKey = "cropImages"
    const val diskCacheKey = "diskCache"
    const val themeModeKey = "themeModeKey"
    const val autoAddToFavoritesKey = "autoAddToFavorites"
    const val grayscaleKey = "grayscale"
    const val blurKey = "blur"
    const val startTabKey = "startTab"
    const val showColorPalette = "showColorPalette"

    const val wallpaperChangerKey = "wallpaperChanger"
    const val wallpaperChangerIntervalKey = "wallpaperChangerInterval"
    private const val wallpaperChangerConfigKey = "wallpaperChangerConfig"
    const val combineWallpaperChangers = "combineWallpaperChangers"

    const val defaultDiskCacheSize = 128L * 1024 * 1024
    const val defaultWallpaperChangeInterval = 15L

    private const val prefFile = "preferences"
    private lateinit var preferences: SharedPreferences

    private val mapper = ObjectMapper()

    fun init(context: Context) {
        preferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE)
    }

    fun getBoolean(key: String, defValue: Boolean) = preferences.getBoolean(key, defValue)
    fun getString(key: String, defValue: String) = preferences.getString(key, defValue) ?: defValue

    fun getFloat(key: String, defValue: Float) = preferences.getFloat(key, defValue)

    fun edit(action: SharedPreferences.Editor.() -> Unit) {
        preferences.edit().apply(action).apply()
    }

    fun getApiByRoute(route: String) = when (route) {
        DrawerScreens.Picsum.route -> PsApi()
        DrawerScreens.OWalls.route -> OwApi()
        DrawerScreens.Unsplash.route -> UsApi()
        DrawerScreens.MiWalls.route -> MiApi()
        DrawerScreens.BingDaily.route -> BiApi()
        else -> WhApi()
    }

    fun setWallpaperConfigs(configs: List<WallpaperConfig>) {
        edit { putString(wallpaperChangerConfigKey, mapper.writeValueAsString(configs)) }
    }

    fun getWallpaperConfigs(): List<WallpaperConfig> {
        val prefString = getString(wallpaperChangerConfigKey, "")
        return try {
            mapper.readValue(
                prefString,
                object : TypeReference<List<WallpaperConfig>>() {})
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.toString())
            listOf(WallpaperConfig(WallpaperTarget.BOTH))
        }
    }
}
