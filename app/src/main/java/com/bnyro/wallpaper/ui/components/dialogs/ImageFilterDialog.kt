package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.ui.components.prefs.CheckboxPref
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
                CheckboxPref(
                    prefKey = Preferences.grayscaleKey,
                    title = stringResource(R.string.grayscale)
                ) {
                    onChange.invoke()
                }
            }
        }
    )
}
