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
    prefKey: String? = null,
    title: String,
    summary: String? = null,
    defaultValue: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    var checked by remember {
        mutableStateOf(
            prefKey?.let { Preferences.getBoolean(it, defaultValue) } ?: defaultValue
        )
    }
    val interactionSource = remember { MutableInteractionSource() }

    fun onChange(newValue: Boolean) {
        checked = newValue
        if (prefKey != null) Preferences.edit { putBoolean(prefKey, checked) }
        onCheckedChange.invoke(checked)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onChange(!checked)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreferenceItem(
            modifier = Modifier.weight(1f),
            title = title,
            summary = summary
        )
        Checkbox(
            checked = checked,
            onCheckedChange = ::onChange
        )
    }
}
