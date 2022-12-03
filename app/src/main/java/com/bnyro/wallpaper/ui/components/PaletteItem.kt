package com.bnyro.wallpaper.ui.components

import androidx.annotation.ColorInt
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.util.ClipboardHelper

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaletteItem(@ColorInt color: Int) {
    val clipboardHelper = ClipboardHelper(LocalContext.current)

    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(Color(color))
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    val hexColor = String.format(
                        "#%06X",
                        0xFFFFFF and color
                    )
                    clipboardHelper.write(hexColor)
                }
            )
    )
}
