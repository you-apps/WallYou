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
import androidx.compose.runtime.LaunchedEffect
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
import com.bnyro.wallpaper.db.DatabaseHolder.Companion.Database
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.ext.query
import com.bnyro.wallpaper.ui.components.WallpaperGrid

@Composable
fun FavoritesPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var favorites by remember {
            mutableStateOf(listOf<Wallpaper>())
        }

        LaunchedEffect(true) {
            query {
                favorites = Database.favoritesDao().getAll()
            }
        }

        if (favorites.isNotEmpty()) {
            WallpaperGrid(
                wallpapers = favorites
            )
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
