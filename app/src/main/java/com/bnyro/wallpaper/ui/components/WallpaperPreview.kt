package com.bnyro.wallpaper.ui.components

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.bnyro.wallpaper.obj.Wallpaper
import com.bnyro.wallpaper.util.WallpaperHelper

@Composable
fun WallpaperPreview(
    wallpaper: Wallpaper,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    var bitmap: Bitmap? = null

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
                    if (bitmap != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            WallpaperHelper.setWallpaper(
                                context.applicationContext,
                                bitmap!!,
                                WallpaperManager.FLAG_SYSTEM
                            )
                        } else {
                            WallpaperHelper.setWallpaperLegacy(
                                context.applicationContext,
                                bitmap!!
                            )
                        }
                    }
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
}
