package com.bnyro.wallpaper.ui.activities

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bnyro.wallpaper.ui.theme.WallYouTheme

open class BaseActivity : ComponentActivity() {
    fun showContent(content: @Composable () -> Unit) {
        setContent {
            WallYouTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    content.invoke()
                }
            }
        }
    }
}
