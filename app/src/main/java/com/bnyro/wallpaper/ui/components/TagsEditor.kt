package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.api.Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsEditor(
    api: Api,
    onModify: () -> Unit
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    var tags by remember {
        mutableStateOf(
            api.getTags()
        )
    }

    fun onChange() {
        api.setTags(tags)
        onModify.invoke()
    }

    LazyRow {
        item {
            ElevatedAssistChip(
                onClick = {
                    showDialog = true
                },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Add, null)
                        Spacer(
                            modifier = Modifier.width(2.dp)
                        )
                        Text(stringResource(R.string.add))
                    }
                }
            )
        }

        items(tags) {
            ElevatedAssistChip(
                onClick = {
                    tags = tags - it
                    onChange()
                },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Remove, null)
                        Spacer(
                            modifier = Modifier.width(2.dp)
                        )
                        Text(it)
                    }
                }
            )
        }
    }

    if (showDialog) {
        var text by remember {
            mutableStateOf("")
        }

        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (text != "") tags = tags + text
                        onChange()
                        showDialog = false
                    }
                ) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text(stringResource(android.R.string.cancel))
                }
            },
            title = {
                Text(
                    text = stringResource(id = R.string.add)
                )
            },
            text = {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    }
                )
            }
        )
    }
}
