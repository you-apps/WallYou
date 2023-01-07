package com.bnyro.wallpaper.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Pix
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
    object Picsum : DrawerScreens(R.string.picsum, "ps", Icons.Default.Pix)

    object OWalls : DrawerScreens(R.string.owalls, "ow", Icons.Default.Launch)
    object Favorites : DrawerScreens(R.string.favorites, "favorites", Icons.Default.Favorite, true)
    object Settings : DrawerScreens(R.string.settings, "settings", Icons.Default.Settings, true)
    object About : DrawerScreens(R.string.about, "about", Icons.Default.Info)
}
