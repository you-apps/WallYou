package com.bnyro.wallpaper.ui.components.prefs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.ui.components.ListDialog
import com.bnyro.wallpaper.util.PrefHolder

@Composable
fun ListPreference(
    prefKey: String,
    title: String,
    summary: String? = null,
    entries: List<String>,
    values: List<String>
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    PreferenceItem(
        title = title,
        summary = summary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 5.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                showDialog = true
            }
            .padding(3.dp, 10.dp)
    )

    if (showDialog) {
        ListDialog(
            items = entries,
            onDismissRequest = {
                showDialog = false
            },
            onClick = {
                PrefHolder.PrefEditor.putString(
                    prefKey,
                    values[it]
                ).apply()
                showDialog = false
            }
        )
    }
}
