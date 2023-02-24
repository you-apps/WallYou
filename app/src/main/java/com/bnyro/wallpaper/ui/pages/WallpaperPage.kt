package com.bnyro.wallpaper.ui.pages

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.ui.components.WallpaperGrid
import com.bnyro.wallpaper.ui.components.WallpaperPreview
import com.bnyro.wallpaper.ui.components.dialogs.FilterDialog
import com.bnyro.wallpaper.ui.models.MainModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WallpaperPage(
    viewModel: MainModel
) {
    val context = LocalContext.current
    var showFilterDialog by remember {
        mutableStateOf(false)
    }

    var selectedWallpaper by remember {
        mutableStateOf<Wallpaper?>(null)
    }

    LaunchedEffect(Unit) {
        viewModel.wallpapers = listOf()
        viewModel.page = 1
        viewModel.fetchWallpapers {
            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (viewModel.wallpapers.isNotEmpty()) {
            WallpaperGrid(
                wallpapers = viewModel.wallpapers
            ) {
                viewModel.fetchWallpapers {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                    /*.combinedClickable(
                        onLongClick = {
                            Toast.makeText(context, R.string.applying_random, Toast.LENGTH_SHORT).show()
                            CoroutineScope(Dispatchers.IO).launch {
                                val wallpaperUrl = viewModel.api.getRandomWallpaperUrl()
                                ImageHelper.urlToBitmap(this, wallpaperUrl, context) {
                                    WallpaperHelper.setWallpaper(context, it, WallpaperMode.BOTH)
                                }
                            }
                        }
                    ),

                     */
                onClick = {
                    selectedWallpaper = viewModel.wallpapers.randomOrNull()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = null
                )
            }

            if (viewModel.api.filters.isNotEmpty() || viewModel.api.supportsTags) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    onClick = {
                        showFilterDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null
                    )
                }
            }
        }

        if (showFilterDialog) {
            FilterDialog(
                api = viewModel.api
            ) { changed ->
                showFilterDialog = false
                if (changed) {
                    viewModel.clearWallpapers()
                    viewModel.fetchWallpapers {
                        Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        selectedWallpaper?.let {
            WallpaperPreview(it) {
                selectedWallpaper = null
            }
        }
    }
}
