package com.bnyro.wallpaper.ui.components.prefs

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.ui.components.BlockButton
import com.bnyro.wallpaper.util.Preferences

@Composable
fun MultiSelectionBlockPreference(
    preferenceKey: String?,
    entries: List<String>,
    values: List<String>,
    defaultSelections: List<Int> = listOf(0),
    onSelectionChange: (List<Int>) -> Unit = {}
) {
    val selected = remember {
        val pref = preferenceKey?.let { Preferences.getString(it, "").split(",") }
        val sel = if (!pref.isNullOrEmpty()) pref.map { values.indexOf(it) } else defaultSelections
        sel.toMutableStateList()
    }

    LazyRow(
        modifier = Modifier.padding(horizontal = 5.dp)
    ) {
        itemsIndexed(entries) { index, it ->
            BlockButton(
                modifier = Modifier.padding(2.dp, 0.dp),
                text = it,
                selected = selected.contains(index)
            ) {
                if (selected.contains(index)) selected.remove(index) else selected.add(index)
                if (preferenceKey != null) Preferences.edit {
                    putString(preferenceKey, values.joinToString(","))
                }
                onSelectionChange.invoke(selected)
            }
        }
    }
}
