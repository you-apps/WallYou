package com.bnyro.wallpaper.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.obj.WallpaperConfig
import com.bnyro.wallpaper.enums.WallpaperSource
import com.bnyro.wallpaper.enums.WallpaperTarget
import com.bnyro.wallpaper.ui.components.prefs.CheckboxPref
import com.bnyro.wallpaper.ui.components.prefs.MultiSelectionBlockPreference
import com.bnyro.wallpaper.ui.components.prefs.ListPreference
import com.bnyro.wallpaper.ui.components.prefs.SettingsCategory
import com.bnyro.wallpaper.ui.nav.DrawerScreens
import com.bnyro.wallpaper.util.PickFolderContract
import com.bnyro.wallpaper.util.str

@Composable
fun WallpaperChangerPref(config: WallpaperConfig, onChange: (WallpaperConfig) -> Unit) {
    val context = LocalContext.current

    val localFolderUris = remember {
        config.localFolderUris.toMutableStateList()
    }

    val localWallpaperDirChooser = rememberLauncherForActivityResult(PickFolderContract()) {
        val uri = it ?: return@rememberLauncherForActivityResult

        config.localFolderUris += uri.toString()
        localFolderUris.add(uri.toString())
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
        defaultValue = wallpaperSource.ordinal.toString()
    ) { newValue ->
        config.source = WallpaperSource.values()[newValue.toInt()]
        wallpaperSource = config.source
        onChange(config)
    }

    Crossfade(targetState = wallpaperSource, label = "wallpaper_source") { state ->
        when (state) {
            WallpaperSource.ONLINE -> {
                var currentSelections = remember {
                    config.selectedApiRoutes.map { route ->
                        DrawerScreens.apiScreens.indexOfFirst { it.route == route }
                    }
                }
                MultiSelectionBlockPreference(
                    preferenceKey = null,
                    entries = DrawerScreens.apiScreens.map { it.title.str() },
                    values = DrawerScreens.apiScreens.map { it.route },
                    defaultSelections = currentSelections
                ) { selections ->
                    config.selectedApiRoutes = selections.map { DrawerScreens.apiScreens[it].route }
                    currentSelections = selections
                    onChange(config)
                }
            }

            WallpaperSource.LOCAL -> Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                SettingsCategory(title = stringResource(R.string.directories))

                localFolderUris.forEach {
                    var selectedDirectoryName by remember {
                        mutableStateOf("")
                    }

                    LaunchedEffect(it) {
                        DocumentFile.fromTreeUri(context, it.toUri())?.let { file ->
                            selectedDirectoryName = file.name ?: it
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))
                    Row(
                        modifier = Modifier.padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(selectedDirectoryName)
                        Spacer(modifier = Modifier.weight(1f))
                        ButtonWithIcon(icon = Icons.Default.Delete) {
                            config.localFolderUris -= it
                            localFolderUris.remove(it)
                            onChange(config)
                        }
                    }
                }

                Button(
                    onClick = {
                        localWallpaperDirChooser.launch(null)
                    }
                ) {
                    Text(stringResource(R.string.choose_dir))
                }
            }

            else -> Unit
        }
    }

    var applyImageFilters by remember {
        mutableStateOf(config.applyImageFilters)
    }
    CheckboxPref(
        prefKey = null,
        title = stringResource(id = R.string.apply_image_filters),
        defaultValue = applyImageFilters
    ) { newValue ->
        config.applyImageFilters = newValue
        applyImageFilters = newValue
        onChange(config)
    }
}
