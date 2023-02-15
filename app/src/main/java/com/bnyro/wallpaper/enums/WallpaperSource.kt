package com.bnyro.wallpaper.enums

enum class WallpaperSource(val value: Int) {
    ONLINE(0),
    FAVORITES(1),
    LOCAL(2);

    companion object {
        fun fromInt(value: Int) = WallpaperSource.values().first { it.value == value }
    }
}
