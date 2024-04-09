package com.bnyro.wallpaper.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun WallpaperInfoItem(
    icon: ImageVector,
    @StringRes title: Int,
    value: String,
    isUrl: Boolean = false
) {
    val uriHandler = LocalUriHandler.current

    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = isUrl) {
                uriHandler.openUri(value)
            },
        headlineContent = { Text(text = stringResource(title)) },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        supportingContent = {
            SelectionContainer {
                Text(text = value)
            }
        }
    )
}