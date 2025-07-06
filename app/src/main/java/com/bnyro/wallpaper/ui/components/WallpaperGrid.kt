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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import com.bnyro.wallpaper.ui.models.MainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperGrid(
    viewModel: MainModel,
    wallpapers: List<Wallpaper>,
    onClickWallpaper: (Int) -> Unit,
    onDeleteWallpaper: ((Wallpaper) -> Unit)? = null,
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
        itemsIndexed(wallpapers, key = { _, wallpaper -> wallpaper.imgSrc }) { index, wallpaper ->
            var liked by remember {
                mutableStateOf(false)
            }

            fun toggleLiked() {
                if (liked) {
                    viewModel.removeFromFavorites(wallpaper)
                } else {
                    viewModel.addToFavorites(wallpaper)
                }

                liked = !liked
            }

            LaunchedEffect(true) {
                withContext(Dispatchers.IO) {
                    liked = Database.favoritesDao().isLiked(wallpaper.imgSrc)
                }
            }

            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    when (value) {
                        SwipeToDismissBoxValue.StartToEnd -> onDeleteWallpaper?.invoke(wallpaper)
                        SwipeToDismissBoxValue.EndToStart -> toggleLiked()
                        SwipeToDismissBoxValue.Settled -> Unit
                    }
                    false
                }
            )
            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    WallpaperDismissBackground(dismissState, cornerShape = shape, liked)
                },
                enableDismissFromEndToStart = true,
                enableDismissFromStartToEnd = onDeleteWallpaper != null
            ) {
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
                            model = wallpaper.preview,
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
                                toggleLiked()
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
