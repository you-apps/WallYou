package com.bnyro.wallpaper.ui.activities

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.bnyro.wallpaper.constants.ThemeMode
import com.bnyro.wallpaper.ui.models.MainModel
import com.bnyro.wallpaper.ui.theme.WallYouTheme

open class BaseActivity : ComponentActivity() {
    fun showContent(content: @Composable () -> Unit) {
        val viewModel: MainModel = ViewModelProvider(this).get()
        setContent {
            WallYouTheme(
                darkTheme = when (viewModel.themeMode) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    else -> isSystemInDarkTheme()
                }
            ) {
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
