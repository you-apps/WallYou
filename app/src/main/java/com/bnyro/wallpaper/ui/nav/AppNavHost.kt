package com.bnyro.wallpaper.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.ui.activities.AboutPage
import com.bnyro.wallpaper.ui.activities.FavoritesPage
import com.bnyro.wallpaper.ui.activities.SettingsPage
import com.bnyro.wallpaper.ui.components.WallpaperPage
import com.bnyro.wallpaper.ui.models.MainModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = DrawerScreens.Wallhaven.route,
    viewModel: MainModel
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(DrawerScreens.Wallhaven.route) {
            viewModel.titleResource = R.string.app_name
            WallpaperPage(
                viewModel = viewModel
            )
        }
        composable(DrawerScreens.Favorites.route) {
            viewModel.titleResource = R.string.favorites
            FavoritesPage()
        }
        composable(DrawerScreens.Settings.route) {
            viewModel.titleResource = R.string.settings
            SettingsPage()
        }
        composable(DrawerScreens.About.route) {
            viewModel.titleResource = R.string.about
            AboutPage()
        }
    }
}
