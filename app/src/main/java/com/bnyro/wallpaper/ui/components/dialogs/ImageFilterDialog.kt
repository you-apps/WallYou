package com.bnyro.wallpaper.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.enums.ResizeMethod
import com.bnyro.wallpaper.ui.components.DialogButton
import com.bnyro.wallpaper.ui.components.ImageFilterSlider
import com.bnyro.wallpaper.ui.components.prefs.CheckboxPref
import com.bnyro.wallpaper.ui.components.prefs.ListPreference
import com.bnyro.wallpaper.util.Preferences

@Composable
fun ImageFilterDialog(
    onDismissRequest: () -> Unit,
    onChange: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        dismissButton = {
            DialogButton(
                stringResource(R.string.reset)
            ) {
                Preferences.edit { putFloat(Preferences.blurKey, 1f) }
                Preferences.edit { putBoolean(Preferences.grayscaleKey, false) }
                Preferences.edit { putString(Preferences.resizeMethodKey, ResizeMethod.NONE.name) }
                onChange.invoke()
                onDismissRequest.invoke()
            }
        },
        confirmButton = {
            DialogButton(
                text = stringResource(android.R.string.ok)
            ) {
                onChange.invoke()
                onDismissRequest.invoke()
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ImageFilterSlider(
                    prefKey = Preferences.blurKey,
                    title = stringResource(R.string.blur),
                    defValue = 0f,
                    valueRange = 0f..25f,
                    onValueChange = {
                        onChange.invoke()
                    }
                )
                ImageFilterSlider(
                    prefKey = Preferences.contrastKey,
                    title = stringResource(R.string.contrast),
                    defValue = 1f,
                    valueRange = 0f..10f,
                    onValueChange = {
                        onChange.invoke()
                    }
                )
                CheckboxPref(
                    prefKey = Preferences.grayscaleKey,
                    title = stringResource(R.string.grayscale)
                ) {
                    onChange.invoke()
                }
                val resizeMethods = listOf(R.string.none, R.string.crop, R.string.zoom, R.string.fit_width, R.string.fit_height)
                ListPreference(
                    prefKey = Preferences.resizeMethodKey,
                    title = stringResource(R.string.resize_method),
                    entries = resizeMethods.map { stringResource(it) },
                    values = ResizeMethod.values().map { it.name },
                    defaultValue = ResizeMethod.ZOOM.name
                )
                CheckboxPref(
                    prefKey = Preferences.invertBitmapBySystemThemeKey,
                    title = stringResource(R.string.invert_wallpaper_by_theme),
                    summary = stringResource(R.string.invert_wallpaper_by_theme_summary)
                )
            }
        }
    )
}
