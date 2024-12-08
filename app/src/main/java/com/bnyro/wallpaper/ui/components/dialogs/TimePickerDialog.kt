package com.bnyro.wallpaper.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bnyro.wallpaper.ui.components.DialogButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialMillis: Long,
    onTimeChange: (Long) -> Unit,
    onDismissRequest: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = (initialMillis / 1000 / 60 / 60).toInt(),
        initialMinute = (initialMillis / 1000 / 60 % 60).toInt(),
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        dismissButton = {
            DialogButton(stringResource(android.R.string.cancel)) {
                onDismissRequest()
            }
        },
        confirmButton = {
            DialogButton(stringResource(android.R.string.ok)) {
                onTimeChange((timePickerState.hour * 60L + timePickerState.minute) * 60 * 1000)
                onDismissRequest()
            }
        },
        text = {
            TimePicker(timePickerState, layoutType = TimePickerLayoutType.Vertical)
        }
    )
}