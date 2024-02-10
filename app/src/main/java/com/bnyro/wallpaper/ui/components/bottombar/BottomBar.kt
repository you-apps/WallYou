package com.bnyro.wallpaper.ui.components.bottombar

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onClickEdit: () -> Unit,
    onClickFavourite: () -> Unit,
    onClickDownload: () -> Unit,
    onClickWallpaper: () -> Unit,
    isFavourite: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        WallpaperViewActions(
            onClickEdit = onClickEdit,
            onClickDownload = onClickDownload,
            onClickWallpaper = onClickWallpaper,
            onClickFavourite = onClickFavourite,
            isFavourite = isFavourite
        )
    }

}