package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.util.ImageFilterHelper
import com.bnyro.wallpaper.util.Preferences

@Composable
fun ImageFilterDialog(
    onDismissRequest: () -> Unit,
    onChange: (ColorFilter, Dp) -> Unit
) {
    fun updateFilter() {
        val (filter, blur) = ImageFilterHelper.getFilter()
        onChange.invoke(filter, blur)
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        dismissButton = {
            DialogButton(
                stringResource(R.string.reset)
            ) {
                Preferences.setFloat(Preferences.saturationKey, 1f)
                Preferences.setFloat(Preferences.blurKey, 0f)
                updateFilter()
                onDismissRequest.invoke()
            }
        },
        confirmButton = {
            DialogButton(
                text = stringResource(android.R.string.ok)
            ) {
                updateFilter()
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
                        updateFilter()
                    }
                )
                ImageFilterSlider(
                    prefKey = Preferences.blurKey,
                    title = stringResource(R.string.blur),
                    defValue = 1f,
                    valueRange = 0f..15f,
                    onValueChange = {
                        updateFilter()
                    }
                )
            }
        }
    )
}
