package com.bnyro.wallpaper.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ButtonWithIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    tooltip: String? = null,
    onClick: () -> Unit
) {
    PlainTooltipBox(
        tooltip = { Text(tooltip.orEmpty()) }
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier.tooltipTrigger()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }
    }
}
