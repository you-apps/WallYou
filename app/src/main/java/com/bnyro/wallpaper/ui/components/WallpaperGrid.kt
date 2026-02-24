package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    isLoadingMore: Boolean = false,
    onDeleteWallpaper: ((Wallpaper) -> Unit)? = null,
    onScrollEnd: () -> Unit = {}
) {
    val listState = rememberLazyGridState()
    val scrollEnded by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false

            val totalItems = listState.layoutInfo.totalItemsCount
            if (totalItems == 0) return@derivedStateOf false

            lastVisibleItem.index >= totalItems - 1 - LOAD_MORE_THRESHOLD_ITEMS
        }
    }

    val dismissShape = RoundedCornerShape(10.dp)
    val cardShape = MaterialTheme.shapes.medium

    LazyVerticalGrid(
        columns = GridCells.Adaptive(MIN_GRID_ITEM_SIZE.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
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
                        else -> Unit
                    }
                    false
                }
            )
            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    WallpaperDismissBackground(cornerShape = dismissShape)
                },
                enableDismissFromStartToEnd = onDeleteWallpaper != null
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .aspectRatio(9 / 16f)
                        .clip(cardShape)
                        .clickable {
                            onClickWallpaper(index)
                        },
                    shape = cardShape
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AsyncImage(
                            model = wallpaper.preview,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.55f)
                                        )
                                    )
                                )
                        )

                        Box(
                            Modifier
                                .padding(top = 8.dp, end = 8.dp)
                                .align(Alignment.TopEnd)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                        ) {
                            ButtonWithIcon(
                                modifier = Modifier,
                                if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                            ) {
                                toggleLiked()
                            }
                        }

                        val title = wallpaper.title?.takeIf { it.isNotBlank() }
                            ?: wallpaper.category?.takeIf { it.isNotBlank() }
                            ?: wallpaper.author?.takeIf { it.isNotBlank() }
                            ?: ""
                        val subtitle = wallpaper.author?.takeIf { it.isNotBlank() }
                            ?: wallpaper.url?.takeIf { it.isNotBlank() }
                            ?: wallpaper.resolution?.takeIf { it.isNotBlank() }
                            ?: ""
                        if (title.isNotBlank()) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = title,
                                    color = Color.White,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.labelLarge
                                )
                                if (subtitle.isNotBlank()) {
                                    Text(
                                        text = subtitle,
                                        color = Color.White.copy(alpha = 0.88f),
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(scrollEnded) {
        if (scrollEnded && !isLoadingMore) onScrollEnd.invoke()
    }
}

const val MIN_GRID_ITEM_SIZE = 150
private const val LOAD_MORE_THRESHOLD_ITEMS = 5
