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
import com.bnyro.wallpaper.util.ApiHolder

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
            viewModel.titleResource = R.string.wallhaven
            viewModel.api = ApiHolder.whApi
            WallpaperPage(viewModel)
        }
        composable(DrawerScreens.Picsum.route) {
            viewModel.titleResource = R.string.picsum
            viewModel.api = ApiHolder.psApi
            WallpaperPage(viewModel)
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
