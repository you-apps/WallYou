package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.util.shimmer

@Composable
fun ShimmerGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(10) {
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmer(600f)
            )
        }
    }
}
