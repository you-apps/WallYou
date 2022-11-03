package com.bnyro.wallpaper.ui.components

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.db.DatabaseHolder.Companion.Database
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.ext.Query
import com.bnyro.wallpaper.util.BitmapProcessor
import com.bnyro.wallpaper.util.DownloadHelper
import com.bnyro.wallpaper.util.ImageHelper
import com.bnyro.wallpaper.util.WallpaperHelper

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WallpaperPreview(
    wallpaper: Wallpaper,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var showModeSelection by remember {
        mutableStateOf(false)
    }

    var showInfoDialog by remember {
        mutableStateOf(false)
    }

    var showFilterDialog by remember {
        mutableStateOf(false)
    }

    var liked by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        Query {
            wallpaper.imgSrc.let { liked = Database.favoritesDao().exists(it) }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("image/png")
    ) {
        DownloadHelper.save(context, it, bitmap)
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (bitmap != null) {
                ZoomableImage(
                    bitmap = bitmap
                )
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(50.dp, 40.dp)
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(50.dp))
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        ButtonWithIcon(
                            icon = Icons.Default.Info
                        ) {
                            showInfoDialog = true
                        }

                        ButtonWithIcon(
                            icon = Icons.Default.DarkMode
                        ) {
                            showFilterDialog = true
                        }

                        ButtonWithIcon(
                            icon = Icons.Default.Wallpaper
                        ) {
                            if (bitmap == null) return@ButtonWithIcon
                            showModeSelection = true
                        }

                        ButtonWithIcon(
                            icon = Icons.Default.Download
                        ) {
                            launcher.launch("${wallpaper.title}.png")
                        }

                        ButtonWithIcon(
                            icon = if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                        ) {
                            liked = !liked
                            Query {
                                if (!liked) {
                                    Database.favoritesDao().delete(wallpaper)
                                } else {
                                    Database.favoritesDao().insertAll(wallpaper)
                                }
                            }
                        }
                    }
                }
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }

    ImageHelper.urlToBitmap(
        rememberCoroutineScope(),
        wallpaper.imgSrc,
        context.applicationContext
    ) {
        bitmap = BitmapProcessor.blur(it, 1f)
        /*
        bitmap = ImageFilterHelper.getBitmapFromColorMatrix(
            ColorFilterGenerator.adjustColor(0, 10, 1,1),
            it
        )

         */
    }

    if (showInfoDialog) {
        ImageInfoDialog(
            wallpaper = wallpaper
        ) {
            showInfoDialog = false
        }
    }

    if (showFilterDialog) {
        ImageFilterDialog(
            onDismissRequest = {
                showFilterDialog = false
            }
        ) {
            //
        }
    }

    if (showModeSelection) {
        ListDialog(
            items = listOf(
                stringResource(R.string.both),
                stringResource(R.string.home),
                stringResource(R.string.lockscreen)
            ),
            onDismissRequest = {
                showModeSelection = false
            },
            onClick = {
                WallpaperHelper.setWallpaper(
                    context.applicationContext,
                    bitmap!!,
                    it
                )
            }
        )
    }
}
