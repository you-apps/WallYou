package com.bnyro.wallpaper.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.ui.models.MainModel
import com.bnyro.wallpaper.ui.pages.AboutPage
import com.bnyro.wallpaper.ui.pages.FavoritesPage
import com.bnyro.wallpaper.ui.pages.SettingsPage
import com.bnyro.wallpaper.ui.pages.WallpaperPage
import com.bnyro.wallpaper.util.Preferences

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
        DrawerScreens.apiScreens.forEach { screen ->
            composable(screen.route) {
                viewModel.titleResource = screen.titleResource
                viewModel.api = Preferences.getApiByRoute(screen.route)
                WallpaperPage(viewModel)
            }
        }
        composable(DrawerScreens.Favorites.route) {
            viewModel.titleResource = R.string.favorites
            FavoritesPage()
        }
        composable(DrawerScreens.Settings.route) {
            viewModel.titleResource = R.string.settings
            SettingsPage(viewModel)
        }
        composable(DrawerScreens.About.route) {
            viewModel.titleResource = R.string.about
            AboutPage()
        }
    }
}
