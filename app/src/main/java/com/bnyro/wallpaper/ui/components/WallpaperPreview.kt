package com.bnyro.wallpaper.ui.components

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.palette.graphics.Palette
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.db.DatabaseHolder.Companion.Database
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.enums.WallpaperTarget
import com.bnyro.wallpaper.ext.awaitQuery
import com.bnyro.wallpaper.ext.query
import com.bnyro.wallpaper.ui.components.dialogs.ImageFilterDialog
import com.bnyro.wallpaper.ui.components.dialogs.ListDialog
import com.bnyro.wallpaper.util.BitmapProcessor
import com.bnyro.wallpaper.util.DownloadHelper
import com.bnyro.wallpaper.util.ImageHelper
import com.bnyro.wallpaper.util.Preferences
import com.bnyro.wallpaper.util.WallpaperHelper
import java.time.Instant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WallpaperPreview(
    wallpaper: Wallpaper,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    var originalBitmap: Bitmap? = remember { null }

    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var showBottomOptions by remember {
        mutableStateOf(true)
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

    var palette by remember {
        mutableStateOf<Palette?>(null)
    }

    fun generateColorPalette() {
        if (Preferences.getBoolean(Preferences.showColorPalette, true)) {
            Palette.from(bitmap!!).generate { nP -> palette = nP }
        }
    }

    LaunchedEffect(true) {
        query {
            wallpaper.imgSrc.let {
                liked = Database.favoritesDao().exists(it)
            }
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
                    bitmap = bitmap,
                    onDoubleClick = {
                        showBottomOptions = !showBottomOptions
                    },
                    onLongPress = {
                        showBottomOptions = !showBottomOptions
                    }
                )
                AnimatedVisibility(
                    visible = showBottomOptions,
                    modifier = Modifier
                        .padding(50.dp, 40.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(50.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                ButtonWithIcon(
                                    icon = Icons.Default.Info,
                                    tooltip = stringResource(R.string.info)
                                ) {
                                    showInfoDialog = true
                                }

                                ButtonWithIcon(
                                    icon = Icons.Default.DarkMode,
                                    tooltip = stringResource(R.string.filter)
                                ) {
                                    showFilterDialog = true
                                }

                                ButtonWithIcon(
                                    icon = Icons.Default.Wallpaper,
                                    tooltip = stringResource(R.string.set_wallpaper)
                                ) {
                                    if (bitmap == null) return@ButtonWithIcon
                                    showModeSelection = true
                                }

                                ButtonWithIcon(
                                    icon = Icons.Default.Download,
                                    tooltip = stringResource(R.string.download)
                                ) {
                                    val prefix = wallpaper.title ?: wallpaper.category ?: wallpaper.author
                                    val timeStamp = Instant.now().epochSecond
                                    launcher.launch("$prefix-$timeStamp.png")
                                }

                                ButtonWithIcon(
                                    icon = if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    tooltip = stringResource(R.string.add_to_favorites)
                                ) {
                                    liked = !liked
                                    query {
                                        if (!liked) {
                                            Database.favoritesDao().delete(wallpaper)
                                        } else {
                                            Database.favoritesDao().insertAll(wallpaper)
                                        }
                                    }
                                }
                            }

                            palette?.let {
                                PaletteRow(it, Modifier.padding(0.dp, 15.dp))
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
        originalBitmap = it
        bitmap = BitmapProcessor.processBitmapByPrefs(it)
        generateColorPalette()
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
            originalBitmap?.let {
                bitmap = BitmapProcessor.processBitmapByPrefs(it)
                generateColorPalette()
            }
        }
    }

    if (showModeSelection) {
        ListDialog(
            title = stringResource(R.string.set_wallpaper),
            items = listOf(
                stringResource(R.string.both),
                stringResource(R.string.home),
                stringResource(R.string.lockscreen)
            ),
            onDismissRequest = {
                showModeSelection = false
            },
            onClick = { index ->
                if (Preferences.getBoolean(Preferences.autoAddToFavoritesKey, false)) {
                    liked = true
                    awaitQuery {
                        Database.favoritesDao().insertAll(wallpaper)
                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    WallpaperHelper.setWallpaper(
                        context.applicationContext,
                        bitmap!!,
                        WallpaperTarget.values()[index]
                    )
                }
                showModeSelection = false
            }
        )
    }
}
