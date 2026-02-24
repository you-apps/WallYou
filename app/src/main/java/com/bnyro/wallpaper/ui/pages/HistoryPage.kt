package com.bnyro.wallpaper.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f)
        ) {
            Text(
                text = stringResource(R.string.history_subtitle, history.size),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        var selectedIndex by remember { mutableStateOf<Int?>(null) }

        if (history.isNotEmpty()) {
            WallpaperGrid(
                viewModel = viewModel,
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
