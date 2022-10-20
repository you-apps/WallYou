package com.bnyro.wallpaper.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.obj.Wallpaper
import com.bnyro.wallpaper.util.WallpaperHelper

@Composable
fun WallpaperPreview(
    wallpaper: Wallpaper,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    var bitmap: Bitmap? = null

    var showModeSelection by remember {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                model = wallpaper.imgSrc,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        bottom = 30.dp
                    ),
                onClick = {
                    if (bitmap == null) return@FloatingActionButton
                    showModeSelection = true
                }
            ) {
                Icon(Icons.Default.Wallpaper, null)
            }
        }
    }

    ImageHelper.urlToBitmap(
        rememberCoroutineScope(),
        wallpaper.imgSrc ?: "",
        context
    ) {
        bitmap = it
    }

    if (showModeSelection) {
        ListDialog(
            items = listOf(
                stringResource(R.string.both),
                stringResource(R.string.system),
                stringResource(R.string.lockscreen)
            ),
            onDismissRequest = {
                showModeSelection = false
                onDismissRequest.invoke()
            },
            onClick = {
                WallpaperHelper.setWallpaper(
                    context.applicationContext,
                    bitmap!!,
                    it
                )
            }
        )
    }
}
