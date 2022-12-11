package com.bnyro.wallpaper.ui.components.prefs

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bnyro.wallpaper.ui.components.BlockButton
import com.bnyro.wallpaper.util.Preferences

@Composable
fun BlockPreference(
    preferenceKey: String,
    entries: List<String>,
    values: List<String>
) {
    var selected by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(Unit) {
        val pref = Preferences.getString(preferenceKey, "")
        if (pref != "") selected = values.indexOf(pref)
    }

    Row {
        entries.forEachIndexed { index, s ->
            BlockButton(text = s, selected = index == selected) {
                Preferences.edit { putString(preferenceKey, values[index]) }
            }
            selected = index
        }
    }
}
