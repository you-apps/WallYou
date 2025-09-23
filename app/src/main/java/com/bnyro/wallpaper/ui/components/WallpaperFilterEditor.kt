package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.enums.ResizeMethod
import com.bnyro.wallpaper.ext.rememberZoomState
import com.bnyro.wallpaper.ext.zoomArea
import com.bnyro.wallpaper.ext.zoomImage
import com.bnyro.wallpaper.ui.components.prefs.CheckboxPref
import com.bnyro.wallpaper.ui.components.prefs.ListPreference
import com.bnyro.wallpaper.ui.models.WallpaperHelperModel
import com.bnyro.wallpaper.util.BitmapProcessor
import com.bnyro.wallpaper.util.Preferences

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
                Preferences.invertKey,
                false
            )
        )
    }
    var contrastValue by remember {
        mutableFloatStateOf(
            Preferences.getFloat(
                Preferences.contrastKey,
                1f
            )
        )
    }
    var hueValue by remember {
        mutableFloatStateOf(
            Preferences.getFloat(
                Preferences.hueKey,
                1f
            )
        )
    }
    var brightnessValue by remember {
        mutableFloatStateOf(
            Preferences.getFloat(
                Preferences.brightnessKey,
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

    FullscreenDialog(
        onDismissRequest = onDismissRequest,
    ) {
        val bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)
        val sheetState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
        BottomSheetScaffold(scaffoldState = sheetState, sheetContent = {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
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
                    valueRange = 0f..1f,
                    onValueChange = {
                        contrastValue = it
                    },
                    onValueChangeFinished = {
                        Preferences.edit {
                            putFloat(Preferences.contrastKey, contrastValue)
                        }
                    }
                )
                ImageFilterSlider(
                    title = stringResource(R.string.brightness),
                    value = brightnessValue,
                    valueRange = 0f..10f,
                    onValueChange = {
                        brightnessValue = it
                    },
                    onValueChangeFinished = {
                        Preferences.edit {
                            putFloat(Preferences.brightnessKey, brightnessValue)
                        }
                    }
                )
                ImageFilterSlider(
                    title = stringResource(R.string.hue),
                    value = hueValue,
                    valueRange = 0f..1f,
                    onValueChange = {
                        hueValue = it
                    },
                    onValueChangeFinished = {
                        Preferences.edit {
                            putFloat(Preferences.hueKey, hueValue)
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
                CheckboxPref(
                    prefKey = Preferences.invertKey,
                    title = stringResource(R.string.invert_wallpaper)
                ) {
                    invertEnabled = it
                }
                ListPreference(
                    prefKey = Preferences.resizeMethodKey,
                    title = stringResource(R.string.resize_method),
                    entries = resizeMethods.map { stringResource(it) },
                    values = ResizeMethod.entries.map { it.name },
                    defaultValue = ResizeMethod.ZOOM.name
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    TextButton(onClick = {
                        contrastValue = 1f
                        brightnessValue = 1f
                        hueValue = 1f
                        blurRadius = 0f
                        grayscaleEnabled = false
                        invertEnabled = false

                        Preferences.edit {
                            putFloat(Preferences.blurKey, 0f)
                            putFloat(Preferences.contrastKey, 1f)
                            putFloat(Preferences.brightnessKey, 1f)
                            putFloat(Preferences.hueKey, 1f)
                            putString(Preferences.resizeMethodKey, ResizeMethod.CROP.name)
                            putBoolean(Preferences.grayscaleKey, false)
                            putBoolean(Preferences.invertKey, false)
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
                val lowRes = rememberAsyncImagePainter(model = wallpaper.preview)
                val colorMatrix =
                    remember(
                        brightnessValue,
                        contrastValue,
                        hueValue,
                        invertEnabled,
                        grayscaleEnabled
                    ) {
                        // grayscale doesn't seem to work the same way here (always looks greenish)
                        val matrixArray = BitmapProcessor.getTransformMatrix(
                            contrastValue,
                            brightnessValue,
                            hueValue,
                            invertEnabled
                        )

                        ColorMatrix(matrixArray).apply {
                            if (grayscaleEnabled) {
                                // grayscaling has to be done with a new matrix because it would
                                // otherwise override all other transformations (contrast, hue, ...)
                                val grayScaleMatrix = ColorMatrix().apply {
                                    setToSaturation(0f)
                                }
                                timesAssign(grayScaleMatrix)
                            }
                        }
                    }

                AsyncImage(
                    model = wallpaper.imgSrc,
                    contentDescription = stringResource(R.string.wallpaper),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(radius = blurRadius.div(5).dp)
                        .zoomImage(zoomState),
                    placeholder = lowRes,
                    colorFilter = ColorFilter.colorMatrix(colorMatrix)
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
