package com.bnyro.wallpaper.ui.components.prefs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PreferenceItem(
    modifier: Modifier = Modifier,
    title: String,
    summary: String? = null
) {
    Row {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            Text(title)
            if (summary != null) {
                Spacer(Modifier.height(2.dp))
                Text(summary, fontSize = 12.sp)
            }
        }
    }
}
