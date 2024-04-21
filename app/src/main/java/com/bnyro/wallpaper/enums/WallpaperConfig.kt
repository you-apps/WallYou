package com.bnyro.wallpaper.enums

import com.bnyro.wallpaper.ui.nav.DrawerScreens

data class WallpaperConfig(
    var target: WallpaperTarget = WallpaperTarget.BOTH,
    var source: WallpaperSource = WallpaperSource.ONLINE,
    var applyImageFilters: Boolean = true,
    var selectedApiRoutes: List<String> = listOf(DrawerScreens.apiScreens[0].route),
    var localFolderUris: List<String> = listOf()
)
