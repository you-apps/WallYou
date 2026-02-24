package com.bnyro.wallpaper.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import kotlin.math.min

@Composable
fun AnimatedBackground(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "wallyou_background")
    val driftX by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "drift_x"
    )
    val driftY by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 22000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "drift_y"
    )
    val pulse by transition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val colorScheme = MaterialTheme.colorScheme
    val baseGradient = remember(colorScheme) {
        listOf(
            colorScheme.background,
            colorScheme.surfaceVariant.copy(alpha = 0.42f),
            colorScheme.background
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        drawRect(brush = Brush.verticalGradient(colors = baseGradient))

        val minDimension = min(size.width, size.height)
        val primaryCenter = Offset(
            x = size.width * (0.12f + 0.75f * driftX),
            y = size.height * (0.08f + 0.55f * driftY)
        )
        val secondaryCenter = Offset(
            x = size.width * (0.78f - 0.55f * driftY),
            y = size.height * (0.18f + 0.62f * driftX)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    colorScheme.primaryContainer.copy(alpha = 0.40f),
                    colorScheme.primary.copy(alpha = 0.08f),
                    colorScheme.background.copy(alpha = 0f)
                ),
                center = primaryCenter,
                radius = minDimension * 0.62f * pulse
            ),
            center = primaryCenter,
            radius = minDimension * 0.62f * pulse
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    colorScheme.secondaryContainer.copy(alpha = 0.32f),
                    colorScheme.secondary.copy(alpha = 0.08f),
                    colorScheme.background.copy(alpha = 0f)
                ),
                center = secondaryCenter,
                radius = minDimension * 0.58f / pulse
            ),
            center = secondaryCenter,
            radius = minDimension * 0.58f / pulse
        )
    }
}
