package com.bnyro.wallpaper.ui.components.prefs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.api.Api
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    onDismissRequest: () -> Unit,
    api: Api,
    filters: Map<String, List<String>>
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest.invoke()
                }
            ) {
                Text(stringResource(android.R.string.ok))
            }
        },
        title = {
            Text(stringResource(R.string.filter))
        },
        text = {
            Column {
                filters.forEach {
                    Text(
                        text = it.key.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                            else it.toString()
                        },
                        fontSize = 14.sp
                    )
                    Spacer(
                        modifier = Modifier
                            .height(5.dp)
                    )
                    Row {
                        var selected by remember {
                            mutableStateOf(
                                api.getPref(it.key, it.value.first())
                            )
                        }
                        it.value.forEach {
                            ElevatedFilterChip(
                                selected = it == selected,
                                onClick = {
                                    selected = it
                                },
                                label = {
                                    Text(
                                        it.replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                Locale.getDefault()
                                            ) else it.toString()
                                        }
                                    )
                                }
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(5.dp)
                    )
                }
            }
        }
    )
}
