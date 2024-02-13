package com.bnyro.wallpaper.ui.components.infosheet

import android.text.format.Formatter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Label
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.db.obj.Wallpaper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperInfoSheet(onDismissRequest: () -> Unit, wallpaper: Wallpaper) {
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(onDismissRequest, sheetState = state) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = stringResource(R.string.colors),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(16.dp))
            InfoSheetColors(wallpaper = wallpaper)
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
            Text(
                text = stringResource(R.string.details),
                style = MaterialTheme.typography.titleLarge
            )
            wallpaper.title?.let {
                ListItem(
                    headlineContent = { Text(text = stringResource(R.string.label)) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Rounded.Label,
                            contentDescription = null
                        )
                    },
                    supportingContent = {
                        Text(text = it)
                    })
            }
            wallpaper.resolution?.let {
                ListItem(
                    headlineContent = { Text(text = stringResource(id = R.string.resolution)) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Rounded.Image,
                            contentDescription = null
                        )
                    },
                    supportingContent = {
                        Text(text = it)
                    })
            }
            wallpaper.fileSize?.let {
                val context = LocalContext.current
                ListItem(
                    headlineContent = { Text(text = stringResource(R.string.file_size)) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Rounded.Storage,
                            contentDescription = null
                        )
                    },
                    supportingContent = {
                        Text(text = Formatter.formatFileSize(context, it))
                    })
            }
        }
    }
}