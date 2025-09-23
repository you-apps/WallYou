package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.bnyro.wallpaper.db.obj.Wallpaper

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WallpaperPageView(
    initialPage: Int,
    wallpapers: List<Wallpaper>,
    onDismissRequest: () -> Unit
) {
    val showUiState = remember { mutableStateOf(true) }
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        initialPageOffsetFraction = 0f
    ) {
        wallpapers.size
    }
    FullscreenDialog(
        onDismissRequest = onDismissRequest,
    ) {
        HorizontalPager(modifier = Modifier.fillMaxSize(), state = pagerState) {
            WallpaperView(
                wallpaper = wallpapers[it],
                showUiState = { showUiState },
                onClickBack = { onDismissRequest.invoke() })
        }
    }
}