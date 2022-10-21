package com.bnyro.wallpaper.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bnyro.wallpaper.ui.activities.AboutPage
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
            WallpaperPage(
                viewModel = viewModel
            )
        }
        composable("About") {
            AboutPage()
        }
    }
}
