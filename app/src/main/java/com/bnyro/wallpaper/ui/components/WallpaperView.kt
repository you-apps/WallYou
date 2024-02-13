package com.bnyro.wallpaper.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.db.DatabaseHolder.Database
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.enums.MultiState
import com.bnyro.wallpaper.ext.query
import com.bnyro.wallpaper.ui.components.bottombar.BottomBar
import com.bnyro.wallpaper.ui.components.bottombar.WallpaperViewTopBar
import com.bnyro.wallpaper.ui.components.dialogs.MultiStateDialog
import com.bnyro.wallpaper.ui.components.infosheet.WallpaperInfoSheet
import com.bnyro.wallpaper.ui.models.WallpaperHelperModel
import com.bnyro.wallpaper.util.rememberZoomState
import com.bnyro.wallpaper.util.zoomArea
import com.bnyro.wallpaper.util.zoomImage
import java.time.Instant

@Composable
fun WallpaperView(
    wallpaper: Wallpaper,
    showUiState: () -> MutableState<Boolean>,
    onClickBack: () -> Unit,
    wallpaperHelperModel: WallpaperHelperModel = viewModel(factory = WallpaperHelperModel.Factory)
) {
    var showUi by showUiState()
    var showEditView by remember { mutableStateOf(false) }
    var showInfoSheet by remember { mutableStateOf(false) }
    var showModeSelection by remember { mutableStateOf(false) }
    var liked by remember { mutableStateOf(false) }
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
        wallpaperHelperModel.saveWallpaper(wallpaper, uri = it)
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures {
                    showUi = !showUi
                }
            },
        contentAlignment = Alignment.Center
    ) {
        val zoomState = rememberZoomState()

        val alpha by animateFloatAsState(
            targetValue = if (showUi) 0.5f else 0f,
            label = "backgroundAlpha",
            animationSpec = tween(500)
        )
        AsyncImage(
            model = wallpaper.thumb ?: wallpaper.imgSrc,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .blur(50.dp)
                .alpha(alpha)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zoomArea(zoomState),
            contentAlignment = Alignment.Center
        ) {
            val lowRes = rememberAsyncImagePainter(model = wallpaper.thumb ?: wallpaper.imgSrc)
            AsyncImage(
                model = wallpaper.imgSrc,
                contentDescription = stringResource(id = R.string.wallpaper),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .zoomImage(zoomState),
                placeholder = lowRes
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                AnimatedVisibility(
                    visible = showUi
                ) {
                    WallpaperViewTopBar(
                        onClickBack = onClickBack,
                        onClickInfo = {
                            showInfoSheet = true
                        },
                        title = wallpaper.title ?: stringResource(id = R.string.wallpaper)
                    )
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = showUi
                ) {
                    BottomBar(
                        modifier = Modifier
                            .padding(bottom = 30.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        onClickEdit = {
                            showEditView = true
                        },
                        onClickWallpaper = {
                            showModeSelection = true
                        },
                        onClickDownload = {
                            val prefix = wallpaper.title ?: wallpaper.category ?: wallpaper.author
                            val timeStamp = Instant.now().epochSecond
                            launcher.launch("$prefix-$timeStamp.png")
                        },
                        onClickFavourite = {
                            liked = !liked
                            query {
                                if (!liked) {
                                    Database.favoritesDao().delete(wallpaper)
                                } else {
                                    Database.favoritesDao().insertAll(wallpaper)
                                }
                            }
                        },
                        isFavourite = liked
                    )
                }
            }
        }
    }
    if (showEditView) {
        WallpaperFilterEditor(wallpaper = wallpaper) {
            showEditView = false
        }
    }
    if (showInfoSheet) {
        WallpaperInfoSheet(onDismissRequest = { showInfoSheet = false }, wallpaper = wallpaper)
    }
    if (showModeSelection) {
        WallpaperModeDialog(
            wallpaper,
            wallpaperHelperModel,
            onDismissRequest = { showModeSelection = false },
            onLike = {
                liked = true
            })
    }

    MultiStateDialog(
        state = wallpaperHelperModel.saveWallpaperState,
        title = stringResource(R.string.saving_wallpaper)
    ) {
        wallpaperHelperModel.saveWallpaperState = MultiState.IDLE
    }
    MultiStateDialog(
        state = wallpaperHelperModel.setWallpaperState,
        title = stringResource(R.string.applying_wallpaper)
    ) {
        wallpaperHelperModel.setWallpaperState = MultiState.IDLE
    }
}

