package com.bnyro.wallpaper.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bnyro.wallpaper.ui.components.DialogButton
import com.bnyro.wallpaper.ui.components.SelectableItem

@Composable
fun ListDialog(
    items: List<String>,
    title: String? = null,
    onDismissRequest: () -> Unit,
    onClick: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            DialogButton(
                text = stringResource(android.R.string.cancel),
                onClick = {
                    onDismissRequest()
                }
            )
        },
        title = {
            title?.let { Text(it) }
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
