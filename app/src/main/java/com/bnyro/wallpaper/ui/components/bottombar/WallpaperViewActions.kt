package com.bnyro.wallpaper.ui.components.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.SaveAlt
import androidx.compose.material.icons.rounded.Wallpaper
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.bnyro.wallpaper.R

@Composable
fun WallpaperViewActions(
    color: Color = Color.White,
    onClickEdit: () -> Unit,
    onClickDownload: () -> Unit,
    onClickWallpaper: () -> Unit,
    onClickFavourite: () -> Unit,
    isFavourite: Boolean
) {
    IconWithText(
        imageVector = Icons.Rounded.Wallpaper,
        title = stringResource(id = R.string.wallpaper),
        color = color
    ) {
        onClickWallpaper.invoke()
    }
    IconWithText(
        imageVector = Icons.Rounded.SaveAlt,
        title = stringResource(R.string.save),
        color = color
    ) {
        onClickDownload.invoke()
    }
    IconWithText(
        imageVector = Icons.Rounded.Edit,
        title = stringResource(R.string.edit),
        color = color
    ) {
        onClickEdit.invoke()
    }
    IconWithText(
        imageVector = if (isFavourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
        title = stringResource(id = R.string.favorite),
        color = color
    ) {
        onClickFavourite.invoke()
    }
}
