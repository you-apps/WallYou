package com.bnyro.wallpaper.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bnyro.wallpaper.R
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
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.HeartBroken,
                        null,
                        Modifier.size(150.dp)
                    )
                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                    Text(
                        stringResource(R.string.no_favorites),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
