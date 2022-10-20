package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun ListDialog(
    items: List<String>,
    onDismissRequest: () -> Unit,
    onClick: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(android.R.string.cancel))
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                items.forEachIndexed { index, title ->
                    SelectableItem(
                        text = title
                    ) {
                        onClick.invoke(index)
                    }
                }
            }
        }
    )
}
