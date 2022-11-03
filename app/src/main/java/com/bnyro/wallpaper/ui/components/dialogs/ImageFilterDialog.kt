package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bnyro.wallpaper.R
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
                Preferences.setFloat(Preferences.saturationKey, 1f)
                Preferences.setFloat(Preferences.blurKey, 0f)
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
                    prefKey = Preferences.saturationKey,
                    title = stringResource(R.string.saturation),
                    defValue = 1f,
                    valueRange = 0f..5f,
                    onValueChange = {
                        onChange.invoke()
                    }
                )
                ImageFilterSlider(
                    prefKey = Preferences.blurKey,
                    title = stringResource(R.string.blur),
                    defValue = 1f,
                    valueRange = 0f..15f,
                    onValueChange = {
                        onChange.invoke()
                    }
                )
            }
        }
    )
}
