package com.bnyro.wallpaper.util

import android.content.Context
import android.content.SharedPreferences
import com.bnyro.wallpaper.api.ow.OwApi
import com.bnyro.wallpaper.api.ps.PsApi
import com.bnyro.wallpaper.api.us.UsApi
import com.bnyro.wallpaper.api.wh.WhApi
import com.bnyro.wallpaper.constants.WallpaperMode
import com.bnyro.wallpaper.enums.WallpaperSource
import com.bnyro.wallpaper.ui.nav.DrawerScreens

object Preferences {
    const val cropImagesKey = "cropImages"
    const val diskCacheKey = "diskCache"
    const val wallpaperChangerKey = "wallpaperChanger"
    const val wallpaperChangerIntervalKey = "wallpaperChangerInterval"
    const val wallpaperChangerTargetKey = "wallpaperChangerTarget"
    const val themeModeKey = "themeModeKey"
    const val wallpaperChangerApiKey = "wallpaperChangerApi"
    const val autoAddToFavoritesKey = "autoAddToFavorites"
    const val grayscaleKey = "grayscale"
    const val blurKey = "blur"
    const val autoChangerSourceKey = "autoChangerSource"
    const val localWallpaperDirKey = "localWallpaperDir"

    const val defaultDiskCacheSize = 128L * 1024 * 1024
    const val defaultWallpaperChangeInterval = 15L
    const val defaultWallpaperChangerTarget = WallpaperMode.BOTH

    private const val prefFile = "preferences"
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE)
    }

    fun getBoolean(key: String, defValue: Boolean) = preferences.getBoolean(key, defValue)
    fun getString(key: String, defValue: String) = preferences.getString(key, defValue)

    fun getFloat(key: String, defValue: Float) = preferences.getFloat(key, defValue)

    fun edit(action: SharedPreferences.Editor.() -> Unit) {
        preferences.edit().apply(action).apply()
    }

    fun getWallpaperChangerApi() = when (getString(wallpaperChangerApiKey, "")) {
        DrawerScreens.Picsum.route -> PsApi()
        DrawerScreens.OWalls.route -> OwApi()
        DrawerScreens.Unsplash.route -> UsApi()
        else -> WhApi()
    }

    fun getChangerSource(): WallpaperSource {
        val pref = getString(autoChangerSourceKey, WallpaperSource.ONLINE.value.toString())
        return WallpaperSource.fromInt(pref?.toInt() ?: 0)
    }
}
