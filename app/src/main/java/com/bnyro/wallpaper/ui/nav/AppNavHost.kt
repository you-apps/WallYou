package com.bnyro.wallpaper.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bnyro.wallpaper.App
import com.bnyro.wallpaper.ui.models.MainModel
import com.bnyro.wallpaper.ui.pages.AboutPage
import com.bnyro.wallpaper.ui.pages.FavoritesPage
import com.bnyro.wallpaper.ui.pages.HistoryPage
import com.bnyro.wallpaper.ui.pages.SettingsPage
import com.bnyro.wallpaper.ui.pages.WallpaperPage
import com.bnyro.wallpaper.util.Preferences

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = App.apis.first().route,
    viewModel: MainModel
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        DrawerScreens.apiScreens.forEach { screen ->
            composable(screen.route) {
                viewModel.titleResource = screen.title
                viewModel.api = Preferences.getApiByRoute(screen.route)
                WallpaperPage(viewModel)
            }
        }
        composable(DrawerScreens.Companion.Favorites.route) {
            viewModel.titleResource = DrawerScreens.Companion.Favorites.title
            FavoritesPage(viewModel)
        }
        composable(DrawerScreens.Companion.History.route) {
            viewModel.titleResource = DrawerScreens.Companion.History.title
            HistoryPage(viewModel)
        }
        composable(DrawerScreens.Companion.Settings.route) {
            viewModel.titleResource = DrawerScreens.Companion.Settings.title
            SettingsPage(viewModel)
        }
        composable(DrawerScreens.Companion.About.route) {
            viewModel.titleResource = DrawerScreens.Companion.About.title
            AboutPage()
        }
    }
}
