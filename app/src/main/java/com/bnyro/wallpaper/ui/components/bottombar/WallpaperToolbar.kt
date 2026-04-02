package com.bnyro.wallpaper.ui.components.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.SaveAlt
import androidx.compose.material.icons.rounded.Wallpaper
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.bnyro.wallpaper.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WallpaperToolbar(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onClickEdit: () -> Unit,
    onClickFavourite: () -> Unit,
    onClickDownload: () -> Unit,
    onClickWallpaper: () -> Unit,
    isFavourite: Boolean
) {
    HorizontalFloatingToolbar(
        modifier = modifier,
        expanded = expanded,
        floatingActionButton = {
            FloatingToolbarDefaults.VibrantFloatingActionButton(
                onClick = onClickWallpaper
            ) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                        TooltipAnchorPosition.Above
                    ),
                    tooltip = { PlainTooltip { Text(stringResource(R.string.edit)) } },
                    state = rememberTooltipState()
                ) {
                    Icon(
                        imageVector = Icons.Default.Wallpaper,
                        contentDescription = stringResource(R.string.wallpaper)
                    )
                }
            }
        }
    ) {
        IconWithText(
            imageVector = Icons.Rounded.SaveAlt,
            title = stringResource(R.string.save),
        ) {
            onClickDownload.invoke()
        }
        IconWithText(
            imageVector = Icons.Rounded.Edit,
            title = stringResource(id = R.string.edit),
        ) {
            onClickEdit.invoke()
        }
        IconWithText(
            imageVector = if (isFavourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
            title = stringResource(id = R.string.favorite),
        ) {
            onClickFavourite.invoke()
        }
    }
}

@Composable
fun IconWithText(
    imageVector: ImageVector,
    title: String,
    onItemClick: () -> Unit,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above
        ),
        tooltip = { PlainTooltip { Text(title) } },
        state = rememberTooltipState()
    ) {
        IconButton(onClick = onItemClick) {
            Icon(
                imageVector = imageVector,
                contentDescription = title
            )
        }
    }
}