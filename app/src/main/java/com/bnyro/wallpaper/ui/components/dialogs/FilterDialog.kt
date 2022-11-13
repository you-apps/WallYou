package com.bnyro.wallpaper.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import com.bnyro.wallpaper.ui.components.DialogButton
import com.bnyro.wallpaper.ui.components.TagsEditor
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    api: Api,
    onDismissRequest: (Boolean) -> Unit
) {
    var modified = false

    AlertDialog(
        onDismissRequest = {
            onDismissRequest.invoke(modified)
        },
        confirmButton = {
            DialogButton(
                text = stringResource(android.R.string.ok),
                onClick = {
                    onDismissRequest.invoke(modified)
                }
            )
        },
        title = {
            Text(stringResource(R.string.filter))
        },
        text = {
            Column {
                if (api.supportsTags) {
                    TagsEditor(
                        api = api
                    ) {
                        modified = true
                    }
                }
                api.filters.forEach {
                    Text(
                        text = it.key.replaceFirstChar {
                            if (it.isLowerCase()) {
                                it.titlecase(Locale.getDefault())
                            } else {
                                it.toString()
                            }
                        },
                        fontSize = 14.sp
                    )
                    Spacer(
                        modifier = Modifier
                            .height(5.dp)
                    )
                    var selected by remember {
                        mutableStateOf(
                            api.getPref(it.key, it.value.first())
                        )
                    }
                    LazyRow {
                        items(it.value) { entry ->
                            ElevatedFilterChip(
                                modifier = Modifier
                                    .padding(2.dp, 0.dp),
                                selected = entry == selected,
                                onClick = {
                                    modified = true
                                    selected = entry
                                    api.setPref(it.key, entry)
                                },
                                label = {
                                    Text(
                                        entry
                                            .replace("_", " ")
                                            .replaceFirstChar {
                                                if (it.isLowerCase()) {
                                                    it.titlecase(
                                                        Locale.getDefault()
                                                    )
                                                } else {
                                                    it.toString()
                                                }
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
