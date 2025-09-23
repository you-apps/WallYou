package com.bnyro.wallpaper.ui.components.infosheet

import android.text.format.Formatter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoSizeSelectLarge
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import com.bnyro.wallpaper.ui.components.WallpaperInfoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperInfoSheet(onDismissRequest: () -> Unit, wallpaper: Wallpaper) {
    val context = LocalContext.current
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(onDismissRequest, sheetState = state) {
        Column(
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.colors),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(16.dp))
            InfoSheetColors(wallpaper = wallpaper)
            HorizontalDivider(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
            Text(
                text = stringResource(R.string.details),
                style = MaterialTheme.typography.titleLarge
            )
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                wallpaper.title?.let {
                    WallpaperInfoItem(Icons.AutoMirrored.Filled.Label, R.string.label, it)
                }
                wallpaper.description?.let {
                    WallpaperInfoItem(Icons.Default.Description, R.string.description, it)
                }
                wallpaper.author?.let {
                    WallpaperInfoItem(Icons.Default.Person, R.string.author, it)
                }
                wallpaper.category?.let {
                    WallpaperInfoItem(Icons.Default.Category, R.string.category, it)
                }
                wallpaper.resolution?.let {
                    WallpaperInfoItem(Icons.Default.PhotoSizeSelectLarge, R.string.resolution, it)
                }
                wallpaper.fileSize?.let {
                    WallpaperInfoItem(
                        Icons.Default.Storage,
                        R.string.fileSize,
                        Formatter.formatFileSize(context, it)
                    )
                }
                wallpaper.creationDate?.let {
                    WallpaperInfoItem(Icons.Default.AccessTime, R.string.creationDate, it)
                }
                wallpaper.url?.let {
                    WallpaperInfoItem(Icons.Default.Web, R.string.source, it, true)
                }
                WallpaperInfoItem(Icons.Default.Image, R.string.image_url, wallpaper.imgSrc, true)
            }
        }
    }
}