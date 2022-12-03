package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.palette.graphics.Palette

@Composable
fun PaletteRow(palette: Palette, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (color in listOf(
            palette.lightVibrantSwatch,
            palette.lightMutedSwatch,
            palette.vibrantSwatch,
            palette.mutedSwatch,
            palette.darkVibrantSwatch,
            palette.darkMutedSwatch
        )) {
            color?.let {
                item {
                    PaletteItem(it.rgb)
                }
            }
        }
    }
}
