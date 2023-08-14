package com.bnyro.wallpaper.util

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * @param length Diagonal length of the element
 */
fun Modifier.shimmer(length: Float): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer transition")
    val offset by transition.animateFloat(
        initialValue = -100f,
        targetValue = length + 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradient offset"
    )
    val color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
    val startOffset = -length / 4 + offset
    val endOffset = length / 4 + offset
    val brush = Brush.linearGradient(
        colors = listOf(
            color.copy(alpha = 0.2f),
            color.copy(alpha = 0.6f),
            color.copy(alpha = 0.2f)
        ),
        start = Offset(x = startOffset, y = startOffset),
        end = Offset(x = endOffset, y = endOffset)
    )
    this.then(background(brush))
}

@Preview
@Composable
private fun ShimmerPreview() {
    Box(
        modifier = Modifier
            .size(300.dp)
            .shimmer(400f)
    )
}
