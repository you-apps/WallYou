package com.bnyro.wallpaper.obj

import com.bnyro.wallpaper.enums.WallpaperSource
import com.bnyro.wallpaper.enums.WallpaperTarget
import com.bnyro.wallpaper.ui.nav.DrawerScreens
import kotlinx.serialization.Serializable

@Serializable
data class WallpaperConfig(
    var target: WallpaperTarget = WallpaperTarget.BOTH,
    var source: WallpaperSource = WallpaperSource.ONLINE,
    var applyImageFilters: Boolean = true,
    var selectedApiRoutes: List<String> = listOf(DrawerScreens.apiScreens[0].route),
    var localFolderUris: List<String> = listOf()
)
