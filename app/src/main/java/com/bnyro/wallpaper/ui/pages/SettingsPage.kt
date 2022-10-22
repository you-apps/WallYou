package com.bnyro.wallpaper.ui.activities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.ui.components.about.AboutContainer
import com.bnyro.wallpaper.ui.components.prefs.CheckboxPref
import com.bnyro.wallpaper.ui.components.prefs.SettingsCategory
import com.bnyro.wallpaper.util.PrefHolder

@Composable
fun SettingsPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AboutContainer {
            Column {
                SettingsCategory(
                    title = stringResource(R.string.general)
                )
                CheckboxPref(
                    prefKey = PrefHolder.cropImagesKey,
                    title = stringResource(R.string.crop_images)
                )
            }
        }
    }
}
