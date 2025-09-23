package com.bnyro.wallpaper.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun FullscreenDialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = remember {
            DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        },
        content = content
    )
}