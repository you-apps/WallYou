package com.bnyro.wallpaper.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun ZoomableImage(
    bitmap: Bitmap?,
    modifier: Modifier,
    contentScale: ContentScale,
    minScale: Float = 0.7f,
    maxScale: Float = 3f,
    backgroundColor: Color = MaterialTheme.colorScheme.background
) {
    val scale = remember { mutableStateOf(1f) }
    // val rotationState = remember { mutableStateOf(0f) }
    Box(
        modifier = Modifier
            .clip(RectangleShape) // Clip the box content
            .fillMaxSize() // Give the size you want...
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, rotation ->
                    scale.value *= zoom
                    // rotationState.value += rotation
                }
            }
    ) {
        AsyncImage(
            model = bitmap,
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = maxOf(minScale, minOf(maxScale, scale.value)),
                    scaleY = maxOf(minScale, minOf(maxScale, scale.value))
                    // rotationZ = rotationState.value
                ),
            contentDescription = null
        )
    }
}
