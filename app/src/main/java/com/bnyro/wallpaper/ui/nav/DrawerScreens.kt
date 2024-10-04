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
import com.bnyro.wallpaper.App
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.util.Either
import com.bnyro.wallpaper.util.Preferences

open class DrawerScreens(
    val title: Either<Int, String>,
    val route: String,
    val icon: ImageVector,
    val divideBefore: Boolean = false
) {
    companion object {
        object Favorites :
            DrawerScreens(Either.Left(R.string.favorites), "favorites", Icons.Default.Favorite, true)

        object History : DrawerScreens(Either.Left(R.string.history), "history", Icons.Default.History)
        object Settings :
            DrawerScreens(Either.Left(R.string.settings), "settings", Icons.Default.Settings, true)

        object About : DrawerScreens(Either.Left(R.string.about), "about", Icons.Default.Info)

        val apiScreens by lazy {
            App.apis.map {
                DrawerScreens(
                    title = Either.Right(it.name),
                    icon = it.icon,
                    route = it.route
                )
            }
        }
        val screens by lazy {
            listOfNotNull(*apiScreens.toTypedArray(), Favorites, History.takeIf {
                Preferences.getBoolean(Preferences.wallpaperHistory, true)
            }, Settings, About)
        }
    }
}