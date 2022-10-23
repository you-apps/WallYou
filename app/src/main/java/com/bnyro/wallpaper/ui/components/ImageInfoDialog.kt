package com.bnyro.wallpaper.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.ext.formatBinarySize

@Composable
fun ImageInfoDialog(
    wallpaper: Wallpaper,
    onDismissRequest: () -> Unit
) {
    val items = mapOf(
        stringResource(R.string.category) to wallpaper.category,
        stringResource(R.string.author) to wallpaper.author,
        stringResource(R.string.resolution) to wallpaper.resolution,
        stringResource(R.string.fileSize) to wallpaper.fileSize?.formatBinarySize(),
        stringResource(R.string.creationDate) to wallpaper.creationDate,
        stringResource(R.string.source) to wallpaper.url
    )

    InfoDialog(
        title = stringResource(R.string.info),
        items = items
    ) {
        onDismissRequest.invoke()
    }
}
