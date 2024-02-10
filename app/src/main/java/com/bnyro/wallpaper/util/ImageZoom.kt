package com.bnyro.wallpaper.util

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

class ZoomState() {
    var scale by mutableFloatStateOf(1f)
    var offsetX by mutableFloatStateOf(1f)
    var offsetY by mutableFloatStateOf(1f)
}

fun Modifier.zoomArea(state: ZoomState): Modifier = this.then(
    pointerInput(Unit) {
        awaitEachGesture {
            awaitFirstDown()
            do {
                val event = awaitPointerEvent()
                if (event.changes.size >= 2) {
                    state.scale *= event.calculateZoom()
                    state.scale = java.lang.Float.max(state.scale, 1f)
                    val offset = event.calculatePan()
                    val w = size.width * (state.scale - 1f) / 2
                    state.offsetX = (state.offsetX + offset.x).coerceIn(-w, w)
                    val h = size.height * (state.scale - 1f) / 2
                    state.offsetY = (state.offsetY + offset.y).coerceIn(-h, h)
                    event.changes.forEach {
                        it.consume()
                    }
                } else if (event.changes.size == 1) {
                    val offset = event.calculatePan()
                    val w = size.width * (state.scale - 1f) / 2
                    state.offsetX = (state.offsetX + offset.x).coerceIn(-w, w)
                    val h = size.height * (state.scale - 1f) / 2
                    state.offsetY = (state.offsetY + offset.y).coerceIn(-h, h)
                    if (state.offsetX != -w && state.offsetX != w) {
                        event.changes.forEach {
                            it.consume()
                        }
                    }
                }
            } while (event.changes.any { it.pressed })
        }
    })

fun Modifier.zoomImage(state: ZoomState): Modifier = this.then(
    graphicsLayer {
        scaleX = state.scale
        scaleY = state.scale
        translationX = state.offsetX
        translationY = state.offsetY
    }
)

@Composable
fun rememberZoomState() = remember { ZoomState() }