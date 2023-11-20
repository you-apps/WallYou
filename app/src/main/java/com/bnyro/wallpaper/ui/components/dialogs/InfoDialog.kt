package com.bnyro.wallpaper.ui.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bnyro.wallpaper.ui.components.DialogButton
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

@Composable
fun InfoDialog(
    title: String,
    items: Map<String, String?>,
    onDismissRequest: () -> Unit
) {
    val localUriHandler = LocalUriHandler.current

    AlertDialog(
        title = {
            Text(title)
        },
        text = {
            Column {
                items.filter { it.value != null }.forEach {
                    Text(
                        text = it.key,
                        fontSize = 12.sp
                    )
                    val isUrl = remember { it.value?.toHttpUrlOrNull() != null }
                    if (isUrl) {
                        val interactionSource = remember {
                            MutableInteractionSource()
                        }
                        Text(
                            modifier = Modifier.clickable(interactionSource, indication = null) {
                                localUriHandler.openUri(it.value!!)
                            },
                            text = it.value!!,
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 18.sp,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    } else {
                        Text(
                            text = it.value!!,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(8.dp)
                    )
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
