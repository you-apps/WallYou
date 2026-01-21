package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.ext.shimmer

@Composable
fun ShimmerGrid() {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(MIN_GRID_ITEM_SIZE.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(10) {
            Box(
                modifier = Modifier
                    .aspectRatio(9 / 16f)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmer(600f)
            )
        }
    }
}
