package com.bnyro.wallpaper.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.work.NetworkType
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.enums.WallpaperSource
import com.bnyro.wallpaper.enums.WallpaperTarget
import com.bnyro.wallpaper.ext.formatMinutes
import com.bnyro.wallpaper.ext.formatTime
import com.bnyro.wallpaper.ext.toast
import com.bnyro.wallpaper.obj.WallpaperConfig
import com.bnyro.wallpaper.ui.components.dialogs.TimePickerDialog
import com.bnyro.wallpaper.ui.components.prefs.CheckboxPref
import com.bnyro.wallpaper.ui.components.prefs.ListPreference
import com.bnyro.wallpaper.ui.components.prefs.MultiSelectionBlockPreference
import com.bnyro.wallpaper.ui.components.prefs.SettingsCategory
import com.bnyro.wallpaper.ui.nav.DrawerScreens
import com.bnyro.wallpaper.util.PickFolderContract
import com.bnyro.wallpaper.util.str

private val networkTypes = listOf(
    R.string.all_networks to NetworkType.CONNECTED,
    R.string.unmetered to NetworkType.UNMETERED,
    R.string.metered to NetworkType.METERED,
    R.string.not_roaming to NetworkType.NOT_ROAMING,
)

@Composable
fun WallpaperChangerPrefDialog(
    config: WallpaperConfig,
    onConfigChange: (WallpaperConfig) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    var wallpaperSource by remember { mutableStateOf(config.source) }
    var wallpaperTarget by remember { mutableStateOf(config.target) }
    var changeInterval by remember { mutableIntStateOf(config.changeIntervalMinutes) }
    var networkType by remember { mutableStateOf(config.networkType) }
    val wallpaperSources =
        listOf(R.string.online, R.string.favorites, R.string.local, R.string.none)
    var wallpaperEnginesIndices = remember {
        config.selectedApiRoutes.map { route ->
            DrawerScreens.apiScreens.indexOfFirst { it.route == route }
        }
    }
    val localFolderUris = remember {
        config.localFolderUris.toMutableStateList()
    }
    var applyImageFilters by remember {
        mutableStateOf(config.applyImageFilters)
    }
    var startTimeMillis by remember {
        mutableStateOf(config.startTimeMillis)
    }
    var endTimeMillis by remember {
        mutableStateOf(config.endTimeMillis)
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        dismissButton = {
            DialogButton(stringResource(android.R.string.cancel)) {
                onDismissRequest()
            }
        },
        confirmButton = {
            DialogButton(stringResource(android.R.string.ok)) {
                if (changeInterval == 0) {
                    context.toast(context.getString(R.string.invalid_time_interval))
                    return@DialogButton
                }

                val hasStartAndEndTime = startTimeMillis != null && endTimeMillis != null
                if (hasStartAndEndTime && startTimeMillis!! == endTimeMillis!!) {
                    context.toast(context.getString(R.string.invalid_time_interval))
                    return@DialogButton
                }

                val newConfig = WallpaperConfig(
                    id = config.id,
                    changeIntervalMinutes = changeInterval,
                    networkType = networkType,
                    target = wallpaperTarget,
                    source = wallpaperSource,
                    selectedApiRoutes = wallpaperEnginesIndices.map { DrawerScreens.apiScreens[it].route },
                    localFolderUris = localFolderUris,
                    applyImageFilters = applyImageFilters,
                    startTimeMillis = if (hasStartAndEndTime) startTimeMillis else null,
                    endTimeMillis = if (hasStartAndEndTime) endTimeMillis else null,
                )
                onConfigChange(newConfig)
                onDismissRequest()
            }
        },
        title = {
            Text(stringResource(R.string.wallpaper_changer))
        },
        text = {
            val localWallpaperDirChooser = rememberLauncherForActivityResult(PickFolderContract()) {
                val uri = it ?: return@rememberLauncherForActivityResult

                config.localFolderUris += uri.toString()
                localFolderUris.add(uri.toString())
            }

            Column {
                MultiSelectionBlockPreference(
                    preferenceKey = null,
                    entries = listOf(R.string.home, R.string.lockscreen).map { stringResource(it) },
                    values = listOf(WallpaperTarget.HOME, WallpaperTarget.LOCK).map { it.name },
                    defaultSelections = when (wallpaperTarget) {
                        WallpaperTarget.BOTH -> listOf(0, 1)
                        WallpaperTarget.HOME -> listOf(0)
                        else -> listOf(1)
                    },
                    requireAtLeastOne = true
                ) { selections ->
                    wallpaperTarget = when {
                        selections.size == 2 -> WallpaperTarget.BOTH
                        selections[0] == 0 -> WallpaperTarget.HOME
                        else -> WallpaperTarget.LOCK
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(stringResource(R.string.change_interval))
                var intervalDays by remember {
                    mutableIntStateOf(changeInterval / 60 / 24)
                }
                var intervalHours by remember {
                    mutableIntStateOf(changeInterval / 60 % 24)
                }
                LaunchedEffect(intervalDays, intervalHours) {
                    changeInterval = ((intervalDays * 24) + intervalHours) * 60
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    NumberPicker(
                        modifier = Modifier.weight(1f),
                        items = (0..90).map { it.toString() },
                        startIndex = intervalDays
                    ) { index ->
                        intervalDays = index
                    }
                    Text(text = stringResource(R.string.days))

                    NumberPicker(
                        modifier = Modifier.weight(1f),
                        items = (0..23).map { it.toString() },
                        startIndex = intervalHours
                    ) { index ->
                        intervalHours = index
                    }
                    Text(text = stringResource(R.string.hours))
                }

                AnimatedVisibility(visible = wallpaperSource != WallpaperSource.LOCAL) {
                    ListPreference(
                        prefKey = null,
                        title = stringResource(R.string.network_type),
                        entries = networkTypes.map { stringResource(id = it.first) },
                        values = networkTypes.map { it.second.name },
                        defaultValue = networkType.name
                    ) {
                        networkType = NetworkType.valueOf(it)
                    }
                }

                ListPreference(
                    prefKey = null,
                    title = stringResource(R.string.wallpaper_changer_source),
                    entries = wallpaperSources.map { stringResource(it) },
                    values = List(wallpaperSources.size) { index -> index.toString() },
                    defaultValue = wallpaperSource.ordinal.toString()
                ) { newValue ->
                    wallpaperSource = WallpaperSource.entries[newValue.toInt()]
                }

                Crossfade(targetState = wallpaperSource, label = "wallpaper_source") { state ->
                    when (state) {
                        WallpaperSource.ONLINE -> Column {
                            MultiSelectionBlockPreference(
                                preferenceKey = null,
                                entries = DrawerScreens.apiScreens.map { it.title.str() },
                                values = DrawerScreens.apiScreens.map { it.route },
                                defaultSelections = wallpaperEnginesIndices
                            ) { selections ->
                                wallpaperEnginesIndices = selections
                            }
                            Spacer(modifier = Modifier.height(5.dp))
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
                                        localFolderUris.remove(it)
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

                CheckboxPref(
                    prefKey = null,
                    title = stringResource(R.string.apply_image_filters),
                    summary = stringResource(R.string.apply_image_filters_desc),
                    defaultValue = applyImageFilters
                ) { newValue ->
                    applyImageFilters = newValue
                }

                var customTimeInterval by remember {
                    mutableStateOf(endTimeMillis != null && startTimeMillis != null)
                }
                CheckboxPref(
                    prefKey = null,
                    title = stringResource(R.string.time_interval),
                    summary = stringResource(R.string.time_interval_desc),
                    defaultValue = customTimeInterval
                ) { newValue ->
                    customTimeInterval = newValue

                    if (!customTimeInterval) {
                        startTimeMillis = null
                        endTimeMillis = null
                    } else {
                        startTimeMillis = 0
                        endTimeMillis = 0
                    }
                }
                AnimatedVisibility(visible = customTimeInterval) {
                    var showStartTimePicker by remember {
                        mutableStateOf(false)
                    }

                    var showEndTimePicker by remember {
                        mutableStateOf(false)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTimeFilled,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Button(
                            onClick = { showStartTimePicker = true }
                        ) {
                            Text((startTimeMillis ?: 0).formatTime())
                        }

                        Icon(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            imageVector = Icons.AutoMirrored.Default.ArrowForward,
                            contentDescription = null
                        )

                        Button(
                            onClick = { showEndTimePicker = true }
                        ) {
                            Text((endTimeMillis ?: 0).formatTime())
                        }
                    }

                    if (showStartTimePicker) {
                        TimePickerDialog(
                            startTimeMillis ?: 0,
                            onTimeChange = { startTimeMillis = it }
                        ) {
                            showStartTimePicker = false
                        }
                    }

                    if (showEndTimePicker) {
                        TimePickerDialog(
                            endTimeMillis ?: 0,
                            onTimeChange = { endTimeMillis = it }
                        ) {
                            showEndTimePicker = false
                        }
                    }
                }
            }
        }
    )
}
