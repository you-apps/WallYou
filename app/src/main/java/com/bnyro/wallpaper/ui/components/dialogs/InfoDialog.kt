package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoDialog(
    title: String,
    items: Map<String, String?>,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        title = {
            Text(title)
        },
        text = {
            Column {
                items.forEach {
                    if (it.value != null) {
                        Text(
                            text = it.key,
                            fontSize = 12.sp
                        )
                        Text(
                            text = it.value!!,
                            fontSize = 18.sp
                        )
                        Spacer(
                            modifier = Modifier
                                .height(8.dp)
                        )
                    }
                }
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            DialogButton(
                text = stringResource(android.R.string.ok),
                onClick = onDismissRequest
            )
        }
    )
}
