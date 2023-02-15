package com.bnyro.wallpaper.ui.components.prefs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.util.Preferences

@Composable
fun CheckboxPref(
    prefKey: String,
    title: String,
    summary: String? = null,
    defaultValue: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    var checked by remember {
        mutableStateOf(
            Preferences.getBoolean(prefKey, defaultValue)
        )
    }
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                checked = !checked
                Preferences.edit { putBoolean(prefKey, checked) }
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreferenceItem(
            title = title,
            summary = summary
        )
        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = it
                Preferences.edit { putBoolean(prefKey, it) }
                onCheckedChange.invoke(it)
            }
        )
    }
}
