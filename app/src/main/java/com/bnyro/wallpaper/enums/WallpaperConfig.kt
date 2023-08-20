package com.bnyro.wallpaper.enums

import com.bnyro.wallpaper.ui.nav.DrawerScreens

data class WallpaperConfig(
    var target: WallpaperTarget = WallpaperTarget.BOTH,
    var source: WallpaperSource = WallpaperSource.ONLINE,
    var apiRoute: String? = DrawerScreens.apiScreens.firstOrNull()?.route,
    var localFolderUri: String? = null
)
