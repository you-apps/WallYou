package com.bnyro.wallpaper.ui.components.bottombar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    contentColor: Color,
    onClickEdit: () -> Unit,
    onClickFavourite: () -> Unit,
    onClickDownload: () -> Unit,
    onClickWallpaper: () -> Unit,
    isFavourite: Boolean
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        WallpaperViewActions(
            onClickEdit = onClickEdit,
            onClickDownload = onClickDownload,
            onClickWallpaper = onClickWallpaper,
            onClickFavourite = onClickFavourite,
            isFavourite = isFavourite,
            color = contentColor
        )
    }

}