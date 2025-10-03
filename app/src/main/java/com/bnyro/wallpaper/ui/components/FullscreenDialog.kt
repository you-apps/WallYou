package com.bnyro.wallpaper.ui.components

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import kotlinx.coroutines.CancellationException

@Composable
fun FullscreenDialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    var alpha by remember {
        mutableFloatStateOf(1f)
    }

    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        ),
        onDismissRequest = onDismissRequest
    ) {
        PredictiveBackHandler { progress ->
            try {
                progress.collect { p ->
                    alpha = 1 - 1.3f * p.progress
                }
                onDismissRequest.invoke()
            } catch (_: CancellationException) {
                alpha = 1f
            }
        }

        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        LaunchedEffect(dialogWindowProvider) {
            dialogWindowProvider.window.setDimAmount(0f)
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha)
        ) {
            content.invoke()
        }
    }
}
