package com.bnyro.wallpaper.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Pix
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.util.Preferences

sealed class DrawerScreens(
    val titleResource: Int,
    val route: String,
    val icon: ImageVector,
    val divideBefore: Boolean = false
) {
    object Wallhaven : DrawerScreens(R.string.wallhaven, "wh", Icons.Default.Landscape)
    object Picsum : DrawerScreens(R.string.picsum, "ps", Icons.Default.Pix)

    object Unsplash : DrawerScreens(R.string.unsplash, "us", Icons.Default.WaterDrop)

    object OWalls : DrawerScreens(R.string.owalls, "ow", Icons.Default.Air)
    object BingDaily : DrawerScreens(R.string.bing_daily, "bi", Icons.Default.Nightlight)
    object Reddit : DrawerScreens(R.string.reddit, "redd", Icons.Default.Forum)
    object Lemmy : DrawerScreens(R.string.lemmy, "le", Icons.Default.Book)
    object Pixel : DrawerScreens(R.string.pixel, "px", Icons.Default.Pix)
    object Spotlight : DrawerScreens(R.string.spotlight, "sp", Icons.Default.LightMode)
    object Favorites : DrawerScreens(R.string.favorites, "favorites", Icons.Default.Favorite, true)
    object History : DrawerScreens(R.string.history, "history", Icons.Default.History)
    object Settings : DrawerScreens(R.string.settings, "settings", Icons.Default.Settings, true)
    object About : DrawerScreens(R.string.about, "about", Icons.Default.Info)

    companion object {
        val apiScreens by lazy {
            listOf(
                Wallhaven,
                Unsplash,
                OWalls,
                Picsum,
                BingDaily,
                Reddit,
                Lemmy,
                Pixel,
                Spotlight
            )
        }
        val screens by lazy {
            listOfNotNull(*apiScreens.toTypedArray(), Favorites, History.takeIf {
                Preferences.getBoolean(Preferences.wallpaperHistory, true)
            }, Settings, About)
        }
    }
}
