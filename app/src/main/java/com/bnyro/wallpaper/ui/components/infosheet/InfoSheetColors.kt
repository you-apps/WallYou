package com.bnyro.wallpaper.ui.components.infosheet

import android.content.ClipData
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.ImageHelper

@Composable
fun InfoSheetColors(
    wallpaper: Wallpaper
) {
    val context = LocalContext.current
    var colors by remember { mutableStateOf(listOf<Palette.Swatch>()) }

    LaunchedEffect(wallpaper) {
        ImageHelper.urlToBitmap(
            this,
            wallpaper.preview,
            context.applicationContext
        ) {
            Palette.from(it).generate { palette ->
                colors = listOfNotNull(
                    palette?.lightVibrantSwatch,
                    palette?.lightMutedSwatch,
                    palette?.vibrantSwatch,
                    palette?.mutedSwatch,
                    palette?.darkVibrantSwatch,
                    palette?.darkMutedSwatch
                )
            }
        }
    }
    val clipboardManager = LocalClipboard.current
    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(items = colors) { colorSwatch ->
            val hexColor = String.format(
                "#%06X",
                0xFFFFFF and colorSwatch.rgb
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(colorSwatch.rgb))
                    .clickable {
                        clipboardManager.nativeClipboard.setPrimaryClip(
                            ClipData(
                                null,
                                arrayOf("text/plain"),
                                ClipData.Item(hexColor)
                            )
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    hexColor,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(colorSwatch.bodyTextColor),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}