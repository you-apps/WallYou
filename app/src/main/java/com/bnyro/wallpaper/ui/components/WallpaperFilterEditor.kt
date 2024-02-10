package com.bnyro.wallpaper.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.enums.ResizeMethod
import com.bnyro.wallpaper.ui.components.prefs.CheckboxPref
import com.bnyro.wallpaper.ui.components.prefs.ListPreference
import com.bnyro.wallpaper.ui.models.WallpaperHelperModel
import com.bnyro.wallpaper.util.Preferences
import com.bnyro.wallpaper.util.rememberZoomState
import com.bnyro.wallpaper.util.zoomArea
import com.bnyro.wallpaper.util.zoomImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperFilterEditor(
    wallpaper: Wallpaper,
    wallpaperHelperModel: WallpaperHelperModel = viewModel(factory = WallpaperHelperModel.Factory),
    onDismissRequest: () -> Unit
) {

    var showModeSelection by remember { mutableStateOf(false) }
    var grayscaleEnabled by remember {
        mutableStateOf(
            Preferences.getBoolean(
                Preferences.grayscaleKey,
                false
            )
        )
    }
    var invertEnabled by remember {
        mutableStateOf(
            Preferences.getBoolean(
                Preferences.invertBitmapBySystemThemeKey,
                false
            )
        )
    }
    var invertPreview by remember {
        mutableStateOf(false)
    }
    var contrastValue by remember {
        mutableFloatStateOf(
            Preferences.getFloat(
                Preferences.contrastKey,
                1f
            )
        )
    }
    var blurRadius by remember {
        mutableFloatStateOf(
            Preferences.getFloat(
                Preferences.blurKey,
                0f
            )
        )
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = remember { DialogProperties(usePlatformDefaultWidth = false) }
    ) {
        val bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)
        val sheetState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
        BottomSheetScaffold(scaffoldState = sheetState, sheetContent = {
            Column {
                ImageFilterSlider(
                    title = stringResource(R.string.blur),
                    value = blurRadius,
                    valueRange = 0f..25f,
                    onValueChange = {
                        blurRadius = it
                    },
                    onValueChangeFinished = {
                        Preferences.edit {
                            putFloat(Preferences.blurKey, blurRadius)
                        }
                    }
                )
                ImageFilterSlider(
                    title = stringResource(R.string.contrast),
                    value = contrastValue,
                    valueRange = 0f..10f,
                    onValueChange = {
                        contrastValue = it
                    },
                    onValueChangeFinished = {
                        Preferences.edit {
                            putFloat(Preferences.contrastKey, contrastValue)
                        }
                    }
                )
                CheckboxPref(
                    prefKey = Preferences.grayscaleKey,
                    title = stringResource(R.string.grayscale)
                ) {
                    grayscaleEnabled = it
                }
                val resizeMethods = listOf(
                    R.string.none,
                    R.string.crop,
                    R.string.zoom,
                    R.string.fit_width,
                    R.string.fit_height
                )
                ListPreference(
                    prefKey = Preferences.resizeMethodKey,
                    title = stringResource(R.string.resize_method),
                    entries = resizeMethods.map { stringResource(it) },
                    values = ResizeMethod.values().map { it.name },
                    defaultValue = ResizeMethod.ZOOM.name
                )
                CheckboxPref(
                    prefKey = Preferences.invertBitmapBySystemThemeKey,
                    title = stringResource(R.string.invert_wallpaper_by_theme),
                    summary = stringResource(R.string.invert_wallpaper_by_theme_summary)
                ) {
                    invertEnabled = it
                    if (!it) invertPreview = false
                }
                AnimatedVisibility(visible = invertEnabled) {
                    Row(
                        modifier = Modifier.padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.preview_invert_effect),
                            style = MaterialTheme.typography.titleSmall
                        )
                        Checkbox(checked = invertPreview, onCheckedChange = {
                            invertPreview = it
                        })
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    TextButton(onClick = {
                        grayscaleEnabled = false
                        contrastValue = 1f
                        blurRadius = 0f
                        invertEnabled = false
                        Preferences.edit {
                            putFloat(Preferences.blurKey, 0f)
                            putFloat(Preferences.contrastKey, 1f)
                            putString(Preferences.resizeMethodKey, ResizeMethod.CROP.name)
                            putBoolean(Preferences.grayscaleKey, false)
                            putBoolean(Preferences.invertBitmapBySystemThemeKey, false)
                        }
                    }) {
                        Text(text = stringResource(R.string.reset))
                    }

                    TextButton(onClick = {
                        onDismissRequest.invoke()
                    }) {
                        Text(text = stringResource(id = R.string.dismiss))
                    }

                    TextButton(onClick = {
                        showModeSelection = true
                    }) {
                        Text(text = stringResource(R.string.apply))
                    }
                }
            }
        }) { pV ->
            val zoomState = rememberZoomState()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pV)
                    .background(Color.Black)
                    .zoomArea(zoomState)
            ) {
                val lowRes = rememberAsyncImagePainter(model = wallpaper.thumb ?: wallpaper.imgSrc)
                val colorMatrix = remember(grayscaleEnabled, invertPreview, contrastValue) {
                    val sat = if (grayscaleEnabled) 0f else 1f
                    val invSat = 1 - sat
                    val R = 0.213f * invSat
                    val G = 0.715f * invSat
                    val B = 0.072f * invSat
                    val invert = if (invertPreview) -1f else 1f
                    val contrast = contrastValue * invert
                    floatArrayOf(
                        (R + sat) * contrast,
                        G * contrast,
                        B * contrast,
                        0f,
                        (255f - 255f * invert) / 2f,
                        R * contrast,
                        (G + sat) * contrast,
                        B * contrast,
                        0f,
                        (255f - 255f * invert) / 2f,
                        R * contrast,
                        G * contrast,
                        (B + sat) * contrast,
                        0f,
                        (255f - 255f * invert) / 2f,
                        0f, 0f, 0f, 1f, 0f
                    )
                }

                AsyncImage(
                    model = wallpaper.imgSrc,
                    contentDescription = stringResource(R.string.wallpaper),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(radius = blurRadius.dp)
                        .zoomImage(zoomState),
                    placeholder = lowRes,
                    colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
                )
            }
        }

    }
    if (showModeSelection) {
        WallpaperModeDialog(
            wallpaper,
            wallpaperHelperModel,
            onDismissRequest = { showModeSelection = false },
            applyFilter = true
        )
    }
}
