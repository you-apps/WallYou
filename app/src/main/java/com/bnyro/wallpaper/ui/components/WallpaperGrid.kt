package com.bnyro.wallpaper.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bnyro.wallpaper.obj.Wallpaper

@Composable
fun WallpaperGrid(
    wallpapers: List<Wallpaper>,
    onScrollEnd: () -> Unit
) {
    val listState = rememberLazyGridState()
    val scrollEnded by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false

            lastVisibleItem.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = listState
    ) {
        items(wallpapers) {
            var showFullscreen by remember {
                mutableStateOf(false)
            }

            ElevatedCard(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(5.dp, 10.dp)
                    .clickable {
                        showFullscreen = true
                    }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(4.dp)
                ) {
                    AsyncImage(
                        model = it.thumb,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            if (showFullscreen) {
                WallpaperPreview(it) {
                    showFullscreen = false
                }
            }
        }
    }

    Log.e("index", scrollEnded.toString())

    if (scrollEnded) {
        LaunchedEffect(Unit) {
            onScrollEnd.invoke()
        }
    }
}
