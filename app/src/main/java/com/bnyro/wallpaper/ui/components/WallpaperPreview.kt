package com.bnyro.wallpaper.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.obj.Wallpaper
import com.bnyro.wallpaper.util.ImageHelper
import com.bnyro.wallpaper.util.WallpaperHelper

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WallpaperPreview(
    wallpaper: Wallpaper,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var showModeSelection by remember {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (bitmap != null) {
                ZoomableImage(
                    bitmap = bitmap
                )
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            bottom = 30.dp,
                            end = 20.dp
                        ),
                    onClick = {
                        if (bitmap == null) return@FloatingActionButton
                        showModeSelection = true
                    }
                ) {
                    Icon(Icons.Default.Wallpaper, null)
                }
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }

    ImageHelper.urlToBitmap(
        rememberCoroutineScope(),
        wallpaper.imgSrc ?: "",
        context.applicationContext
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
