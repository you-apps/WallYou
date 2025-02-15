package com.bnyro.wallpaper.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.ui.components.NothingHere
import com.bnyro.wallpaper.ui.components.WallpaperGrid
import com.bnyro.wallpaper.ui.components.WallpaperPageView
import com.bnyro.wallpaper.ui.models.MainModel

@Composable
fun FavoritesPage(viewModel: MainModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val favorites by viewModel.favWallpapers.collectAsState()

        var selectedIndex by remember { mutableStateOf<Int?>(null) }

        if (favorites.isNotEmpty()) {
            WallpaperGrid(
                viewModel = viewModel,
                wallpapers = favorites,
                onClickWallpaper = {
                    selectedIndex = it
                }
            )
            selectedIndex?.let {
                WallpaperPageView(initialPage = it, wallpapers = favorites) {
                    selectedIndex = null
                }
            }
        } else {
            NothingHere(
                text = stringResource(R.string.no_favorites),
                icon = Icons.Default.HeartBroken
            )
        }
    }
}
