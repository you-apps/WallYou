package com.bnyro.wallpaper.ui.pages

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.constants.ThemeMode
import com.bnyro.wallpaper.ext.formatBinarySize
import com.bnyro.wallpaper.ext.formatMinutes
import com.bnyro.wallpaper.ui.components.about.AboutContainer
import com.bnyro.wallpaper.ui.components.prefs.BlockPreference
import com.bnyro.wallpaper.ui.components.prefs.CheckboxPref
import com.bnyro.wallpaper.ui.components.prefs.ListPreference
import com.bnyro.wallpaper.ui.components.prefs.SettingsCategory
import com.bnyro.wallpaper.ui.models.MainModel
import com.bnyro.wallpaper.ui.nav.DrawerScreens
import com.bnyro.wallpaper.util.Preferences
import com.bnyro.wallpaper.util.WorkerHelper
import java.io.File
import java.time.Instant
import kotlinx.coroutines.launch

@Composable
fun SettingsPage(
    viewModel: MainModel
) {
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()

    val selectFiles = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) {
        context.filesDir.listFiles().orEmpty().forEach { file ->
            file.delete()
        }
        scope.launch {
            it.parallelStream().forEach { uri ->
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    File(context.filesDir, Instant.now().epochSecond.toString()).apply {
                        createNewFile()
                        outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                }
            }
        }
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        AboutContainer {
            Column {
                SettingsCategory(
                    title = stringResource(R.string.general)
                )
                ListPreference(
                    prefKey = Preferences.themeModeKey,
                    title = stringResource(R.string.theme_mode),
                    entries = listOf(
                        stringResource(R.string.theme_system),
                        stringResource(R.string.theme_light),
                        stringResource(R.string.theme_dark)
                    ),
                    values = (0..2).map { it.toString() },
                    defaultValue = ThemeMode.AUTO.toString()
                ) {
                    viewModel.themeMode = it.toInt()
                }
                CheckboxPref(
                    prefKey = Preferences.cropImagesKey,
                    title = stringResource(R.string.crop_images)
                )
                CheckboxPref(
                    prefKey = Preferences.autoAddToFavoritesKey,
                    title = stringResource(R.string.auto_add_to_favorites)
                )
            }
        }

        val cacheSizes = listOf(
            16L,
            32L,
            64L,
            128L,
            256L
        ).map { it * 1024L * 1024L }

        AboutContainer {
            Column {
                SettingsCategory(
                    title = stringResource(R.string.cache)
                )
                ListPreference(
                    prefKey = Preferences.diskCacheKey,
                    title = stringResource(R.string.coil_cache),
                    entries = cacheSizes.map { it.formatBinarySize() },
                    values = cacheSizes.map { it.toString() },
                    defaultValue = Preferences.defaultDiskCacheSize.toString()
                )
            }
        }

        val changeIntervals = listOf(
            15L,
            30L,
            60L,
            180L,
            360L,
            720L,
            1440L
        )

        AboutContainer {
            SettingsCategory(
                title = stringResource(R.string.wallpaper_changer)
            )
            Spacer(
                modifier = Modifier
                    .height(5.dp)
            )
            CheckboxPref(
                prefKey = Preferences.wallpaperChangerKey,
                title = stringResource(R.string.wallpaper_changer)
            ) {
                WorkerHelper.enqueue(context, true)
            }
            var useLocalWallpapers by remember {
                mutableStateOf(Preferences.getBoolean(Preferences.autoChangerLocal, false))
            }
            CheckboxPref(
                prefKey = Preferences.autoChangerLocal,
                title = stringResource(R.string.local_walls),
                summary = stringResource(R.string.local_walls_summary)
            ) {
                WorkerHelper.enqueue(context, true)
                useLocalWallpapers = it
                if (it) selectFiles.launch(arrayOf("image/*"))
            }
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            AnimatedVisibility(visible = !useLocalWallpapers) {
                BlockPreference(
                    preferenceKey = Preferences.wallpaperChangerApiKey,
                    entries = DrawerScreens.apiScreens.map { stringResource(it.titleResource) },
                    values = DrawerScreens.apiScreens.map { it.route }
                ) {
                    WorkerHelper.enqueue(context, true)
                }
            }
            ListPreference(
                prefKey = Preferences.wallpaperChangerIntervalKey,
                title = stringResource(R.string.change_interval),
                entries = changeIntervals.map { it.formatMinutes() },
                values = changeIntervals.map { it.toString() },
                defaultValue = Preferences.defaultWallpaperChangeInterval.toString()
            ) {
                WorkerHelper.enqueue(context, true)
            }
            ListPreference(
                prefKey = Preferences.wallpaperChangerTargetKey,
                title = stringResource(R.string.change_target),
                entries = listOf(
                    stringResource(R.string.both),
                    stringResource(R.string.home),
                    stringResource(R.string.lockscreen)
                ),
                values = (0..2).map { it.toString() },
                defaultValue = Preferences.defaultWallpaperChangerTarget.toString()
            )
        }
    }
}
