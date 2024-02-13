package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bnyro.wallpaper.db.DatabaseHolder.Database
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.ext.query

@Composable
fun WallpaperGrid(
    wallpapers: List<Wallpaper>,
    onClickWallpaper: (Int) -> Unit,
    onScrollEnd: () -> Unit = {}
) {
    val listState = rememberLazyGridState()
    val scrollEnded by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false

            lastVisibleItem.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    val shape = RoundedCornerShape(10.dp)

    LazyVerticalGrid(
        columns = GridCells.Adaptive(170.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        itemsIndexed(wallpapers) { index, wallpaper ->
            var liked by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(true) {
                query {
                    wallpaper.imgSrc.let { src -> liked = Database.favoritesDao().exists(src) }
                }
            }

            ElevatedCard(
                modifier = Modifier
                    .aspectRatio(9 / 16f)
                    .clip(shape)
                    .clickable {
                        onClickWallpaper(index)
                    }
            ) {
                Box {
                    AsyncImage(
                        model = wallpaper.thumb ?: wallpaper.imgSrc,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape)
                    )
                    Box(
                        Modifier
                            .padding(bottom = 8.dp, end = 8.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        ButtonWithIcon(
                            modifier = Modifier,
                            if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                        ) {
                            liked = !liked
                            query {
                                if (!liked) {
                                    Database.favoritesDao().delete(wallpaper)
                                } else {
                                    Database.favoritesDao().insertAll(wallpaper)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (scrollEnded) {
        LaunchedEffect(Unit) {
            onScrollEnd.invoke()
        }
    }
}
