package com.bnyro.wallpaper.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.ui.graphics.vector.ImageVector
import com.bnyro.wallpaper.R

sealed class DrawerScreens(
    val titleResource: Int,
    val route: String,
    val icon: ImageVector,
    val divideBefore: Boolean = false
) {
    object Wallhaven : DrawerScreens(R.string.wallhaven, "wh", Icons.Default.Wallpaper)
    object Favorites : DrawerScreens(R.string.favorites, "favorites", Icons.Default.Favorite, true)
    object Settings : DrawerScreens(R.string.settings, "settings", Icons.Default.Settings, true)
    object About : DrawerScreens(R.string.about, "about", Icons.Default.Info)
}
