package com.bnyro.wallpaper.ui.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.api.CommunityApi
import com.bnyro.wallpaper.ui.components.ShimmerGrid
import com.bnyro.wallpaper.ui.components.WallpaperGrid
import com.bnyro.wallpaper.ui.components.WallpaperPageView
import com.bnyro.wallpaper.ui.components.dialogs.FilterDialog
import com.bnyro.wallpaper.ui.models.MainModel
import kotlinx.coroutines.CancellationException
import kotlin.random.Random

@Composable
fun WallpaperPage(
    viewModel: MainModel
) {
    val context = LocalContext.current
    var showFilterDialog by remember {
        mutableStateOf(false)
    }

    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    var fetchedWallpapers by rememberSaveable {
        mutableStateOf(false)
    }

    fun fetchMoreWallpapers() {
        viewModel.fetchWallpapers { exception, responsibleApi ->
            // suppress errors when a request is cancelled by user navigation
            if (exception !is CancellationException) {
                Toast.makeText(
                    context,
                    "${responsibleApi.name}: ${exception.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        // prevent loading the same page a second time
        if (fetchedWallpapers) return@LaunchedEffect

        viewModel.clearWallpapers()
        fetchMoreWallpapers()

        fetchedWallpapers = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (viewModel.wallpapers.isNotEmpty()) {
            WallpaperGrid(
                viewModel = viewModel,
                wallpapers = viewModel.wallpapers,
                onClickWallpaper = {
                    selectedIndex = it
                }
            ) {
                fetchMoreWallpapers()
            }
            selectedIndex?.let {
                WallpaperPageView(initialPage = it, wallpapers = viewModel.wallpapers) {
                    selectedIndex = null
                }
            }
        } else {
            ShimmerGrid()
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
                onClick = {
                    selectedIndex =
                        if (viewModel.wallpapers.isNotEmpty()) Random.nextInt(viewModel.wallpapers.size) else null
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = null
                )
            }

            if (viewModel.api.filters.isNotEmpty() ||
                viewModel.api.supportsTags ||
                viewModel.api is CommunityApi
            ) {
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
                    fetchMoreWallpapers()
                }
            }
        }
    }
}
