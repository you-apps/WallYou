package com.bnyro.wallpaper.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.constants.Destination
import com.bnyro.wallpaper.ui.activities.AboutPage
import com.bnyro.wallpaper.ui.activities.SettingsPage
import com.bnyro.wallpaper.ui.components.WallpaperPage
import com.bnyro.wallpaper.ui.models.MainModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "Wallhaven",
    viewModel: MainModel
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("Wallhaven") {
            viewModel.titleResource = R.string.app_name
            WallpaperPage(
                viewModel = viewModel
            )
        }
        composable(Destination.SETTINGS) {
            viewModel.titleResource = R.string.settings
            SettingsPage()
        }
        composable(Destination.ABOUT) {
            viewModel.titleResource = R.string.about
            AboutPage()
        }
    }
}
