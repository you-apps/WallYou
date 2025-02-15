package com.bnyro.wallpaper.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
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
fun HistoryPage(viewModel: MainModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val history by viewModel.recentlyAppliedWallpapers.collectAsState()

        var selectedIndex by remember { mutableStateOf<Int?>(null) }

        if (history.isNotEmpty()) {
            WallpaperGrid(
                wallpapers = history,
                onClickWallpaper = {
                    selectedIndex = it
                },
                onDeleteWallpaper = { wallpaper ->
                    viewModel.removeRecentlyAppliedWallpaper(wallpaper)
                }
            )
            selectedIndex?.let {
                WallpaperPageView(initialPage = it, wallpapers = history) {
                    selectedIndex = null
                }
            }
        } else {
            NothingHere(
                text = stringResource(R.string.no_history),
                icon = Icons.Default.History
            )
        }
    }
}
