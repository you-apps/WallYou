package com.bnyro.wallpaper.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.enums.WallpaperConfig
import com.bnyro.wallpaper.enums.WallpaperSource
import com.bnyro.wallpaper.enums.WallpaperTarget
import com.bnyro.wallpaper.ui.components.prefs.BlockPreference
import com.bnyro.wallpaper.ui.components.prefs.ListPreference
import com.bnyro.wallpaper.ui.components.prefs.SettingsCategory
import com.bnyro.wallpaper.ui.nav.DrawerScreens
import com.bnyro.wallpaper.util.LocalWallpaperHelper
import com.bnyro.wallpaper.util.PickFolderContract

@Composable
fun WallpaperChangerPref(config: WallpaperConfig, onChange: (WallpaperConfig) -> Unit) {
    val context = LocalContext.current

    var selectedDirectoryName by remember {
        mutableStateOf<String?>(null)
    }

    fun updateSelectedDirectoryName() {
        val uri = config.localFolderUri?.toUri() ?: return
        DocumentFile.fromTreeUri(context, uri)?.let { file ->
            selectedDirectoryName = file.name
        }
    }

    LaunchedEffect(Unit) {
        updateSelectedDirectoryName()
    }

    val localWallpaperDirChooser = rememberLauncherForActivityResult(PickFolderContract()) {
        val uri = it ?: return@rememberLauncherForActivityResult
        config.localFolderUri = uri.toString()
        updateSelectedDirectoryName()
        onChange(config)
    }

    SettingsCategory(
        title = stringResource(
            when (config.target) {
                WallpaperTarget.BOTH -> R.string.both
                WallpaperTarget.HOME -> R.string.home
                WallpaperTarget.LOCK -> R.string.lockscreen
            }
        )
    )
    val wallpaperSources = listOf(R.string.online, R.string.favorites, R.string.local, R.string.none)
    var wallpaperSource by remember { mutableStateOf(config.source) }
    ListPreference(
        prefKey = null,
        title = stringResource(R.string.wallpaper_changer_source),
        entries = wallpaperSources.map { stringResource(it) },
        values = List(wallpaperSources.size) { index -> index.toString() },
        defaultValue = wallpaperSource.value.toString()
    ) { newValue ->
        config.source = WallpaperSource.fromInt(newValue.toInt())
        wallpaperSource = config.source
        onChange(config)
    }

    Crossfade(targetState = wallpaperSource, label = "wallpaper_source") { state ->
        when (state) {
            WallpaperSource.ONLINE -> {
                var currentIndex by remember {
                    mutableIntStateOf(
                        DrawerScreens.apiScreens.indexOfFirst { it.route == config.apiRoute }
                    )
                }
                BlockPreference(
                    preferenceKey = null,
                    entries = DrawerScreens.apiScreens.map { stringResource(it.titleResource) },
                    values = DrawerScreens.apiScreens.map { it.route },
                    defaultSelection = currentIndex
                ) { index ->
                    config.apiRoute = DrawerScreens.apiScreens[index].route
                    currentIndex = index
                    onChange(config)
                }
            }

            WallpaperSource.LOCAL -> Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        localWallpaperDirChooser.launch(config.localFolderUri?.toUri())
                    }
                ) {
                    Text(stringResource(R.string.choose_dir))
                }
                selectedDirectoryName?.let { selectedDirectoryName ->
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("${stringResource(R.string.current_directory)}: $selectedDirectoryName")
                }
            }

            else -> Unit
        }
    }
}
