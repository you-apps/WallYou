package com.bnyro.wallpaper.ui.pages

import android.annotation.SuppressLint
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.enums.ThemeMode
import com.bnyro.wallpaper.ext.formatBinarySize
import com.bnyro.wallpaper.obj.WallpaperConfig
import com.bnyro.wallpaper.ui.components.ButtonWithIcon
import com.bnyro.wallpaper.ui.components.WallpaperChangerPrefDialog
import com.bnyro.wallpaper.ui.components.prefs.CheckboxPref
import com.bnyro.wallpaper.ui.components.prefs.ListPreference
import com.bnyro.wallpaper.ui.models.MainModel
import com.bnyro.wallpaper.util.BackupHelper
import com.bnyro.wallpaper.util.Preferences
import com.bnyro.wallpaper.util.WorkerHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
private fun SettingsHero(themeLabel: String, cacheLabel: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large,
        tonalElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.settings_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SettingsBadge(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.current_theme),
                    value = themeLabel
                )
                SettingsBadge(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.current_cache),
                    value = cacheLabel
                )
            }
        }
    }
}

@Composable
private fun SettingsBadge(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun SettingsPage(
    viewModel: MainModel
) {
    val context = LocalContext.current.applicationContext
    val activity = LocalActivity.current
    val wallpaperConfigs = remember {
        Preferences.getWallpaperConfigs().toMutableStateList()
    }
    val scope = rememberCoroutineScope()

    val createFavoritesBackup =
        rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(BackupHelper.JSON_MIME)) {
            scope.launch(Dispatchers.IO) {
                BackupHelper.backupFavorites(it ?: return@launch, context)
            }
        }
    val restoreFavoritesBackup =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
            scope.launch(Dispatchers.IO) {
                BackupHelper.restoreFavorites(it ?: return@launch, context)
            }
        }

    val cacheSizes = listOf(
        16L,
        32L,
        64L,
        128L,
        256L
    ).map { it * 1024L * 1024L }

    var selectedCacheSize by remember {
        mutableLongStateOf(
            Preferences.getString(
                Preferences.diskCacheKey,
                Preferences.defaultDiskCacheSize.toString()
            ).toLongOrNull() ?: Preferences.defaultDiskCacheSize
        )
    }
    var wallpaperChangerEnabled by remember {
        mutableStateOf(Preferences.getBoolean(Preferences.wallpaperChangerKey, false))
    }

    val themeModeLabel = when (viewModel.themeMode) {
        ThemeMode.AUTO -> stringResource(R.string.theme_system)
        ThemeMode.LIGHT -> stringResource(R.string.theme_light)
        ThemeMode.DARK -> stringResource(R.string.theme_dark)
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.22f)
                    )
                )
            )
            .verticalScroll(scrollState)
            .padding(bottom = 20.dp)
    ) {
        SettingsHero(
            themeLabel = themeModeLabel,
            cacheLabel = selectedCacheSize.formatBinarySize()
        )

        SettingsSection(
            title = stringResource(R.string.general),
            subtitle = stringResource(R.string.settings_general_desc)
        ) {
            ListPreference(
                prefKey = Preferences.themeModeKey,
                title = stringResource(R.string.theme_mode),
                entries = listOf(
                    stringResource(R.string.theme_system),
                    stringResource(R.string.theme_light),
                    stringResource(R.string.theme_dark)
                ),
                values = (0..2).map { it.toString() },
                defaultValue = ThemeMode.LIGHT.value.toString()
            ) {
                val mode = it.toIntOrNull()
                    ?.let { index -> ThemeMode.entries.getOrNull(index) }
                    ?: ThemeMode.LIGHT
                viewModel.updateThemeMode(mode)
            }
            CheckboxPref(
                prefKey = Preferences.wallpaperHistory,
                title = stringResource(R.string.wallpaper_history),
                defaultValue = true
            )
            CheckboxPref(
                prefKey = Preferences.autoLightenDarkenKey,
                title = stringResource(R.string.auto_darken_lighten_by_theme),
                summary = stringResource(R.string.auto_darken_lighten_by_theme_desc)
            )
        }

        SettingsSection(
            title = stringResource(R.string.cache),
            subtitle = stringResource(R.string.settings_cache_desc)
        ) {
            ListPreference(
                prefKey = Preferences.diskCacheKey,
                title = stringResource(R.string.coil_cache),
                entries = cacheSizes.map { it.formatBinarySize() },
                values = cacheSizes.map { it.toString() },
                defaultValue = Preferences.defaultDiskCacheSize.toString()
            ) { selectedValue ->
                selectedCacheSize = selectedValue.toLongOrNull() ?: Preferences.defaultDiskCacheSize
            }
        }

        SettingsSection(
            title = stringResource(R.string.wallpaper_changer),
            subtitle = stringResource(R.string.settings_wallpaper_changer_desc)
        ) {
            CheckboxPref(
                prefKey = Preferences.wallpaperChangerKey,
                title = stringResource(R.string.wallpaper_changer)
            ) { newValue ->
                wallpaperChangerEnabled = newValue

                WorkerHelper.enqueueOrCancelAll(context, wallpaperConfigs)

                // request unrestricted battery usage if not yet granted
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && newValue) {
                    val pm = context.getSystemService(POWER_SERVICE) as PowerManager

                    if (!pm.isIgnoringBatteryOptimizations(context.packageName)) {
                        val intent = Intent().apply {
                            @SuppressLint("BatteryLife")
                            action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                            data = "package:${context.packageName}".toUri()
                        }
                        activity?.startActivity(intent)
                    }
                }
            }

            AnimatedVisibility(visible = wallpaperChangerEnabled) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var wallpaperConfigDialogIndex by remember {
                        mutableStateOf<Int?>(null)
                    }

                    wallpaperConfigs.forEachIndexed { index, wallpaperConfig ->
                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = wallpaperConfig.getSummary(context),
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                ButtonWithIcon(
                                    icon = Icons.Default.Edit,
                                    tooltip = stringResource(R.string.edit)
                                ) {
                                    wallpaperConfigDialogIndex = index
                                }

                                ButtonWithIcon(
                                    icon = Icons.Default.Delete,
                                    tooltip = stringResource(R.string.dismiss)
                                ) {
                                    WorkerHelper.cancelWork(context, wallpaperConfig)
                                    wallpaperConfigs.removeAt(index)
                                    Preferences.setWallpaperConfigs(wallpaperConfigs)
                                }
                            }
                        }
                    }

                    wallpaperConfigDialogIndex?.let { index ->
                        WallpaperChangerPrefDialog(
                            wallpaperConfigs[index],
                            onConfigChange = { newConfig ->
                                wallpaperConfigs[index] = newConfig
                                Preferences.setWallpaperConfigs(wallpaperConfigs)
                                WorkerHelper.enqueue(context, newConfig, true)
                            },
                            onDismissRequest = { wallpaperConfigDialogIndex = null }
                        )
                    }

                    var newWallpaperConfig by remember {
                        mutableStateOf<WallpaperConfig?>(null)
                    }

                    FilledTonalButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 8.dp),
                        onClick = {
                            val maxId = if (wallpaperConfigs.isNotEmpty()) {
                                wallpaperConfigs.maxBy { it.id }.id
                            } else {
                                -1
                            }

                            newWallpaperConfig = WallpaperConfig(id = maxId + 1)
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            Spacer(Modifier.width(6.dp))
                            Text(stringResource(R.string.add_wallpaper_changer_rule))
                        }
                    }

                    newWallpaperConfig?.let { config ->
                        WallpaperChangerPrefDialog(
                            config,
                            onConfigChange = { newConfig ->
                                wallpaperConfigs.add(newConfig)
                                Preferences.setWallpaperConfigs(wallpaperConfigs)
                                WorkerHelper.enqueue(context, newConfig, true)
                            },
                            onDismissRequest = { newWallpaperConfig = null }
                        )
                    }
                }
            }
        }

        SettingsSection(
            title = stringResource(R.string.import_export),
            subtitle = stringResource(R.string.settings_backup_desc)
        ) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (maxWidth < 430.dp) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                restoreFavoritesBackup.launch(arrayOf(BackupHelper.JSON_MIME))
                            }
                        ) {
                            Text(stringResource(R.string.import_favorites))
                        }
                        FilledTonalButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                createFavoritesBackup.launch("wall_you_favorites_backup.json")
                            }
                        ) {
                            Text(stringResource(R.string.export_favorites))
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                restoreFavoritesBackup.launch(arrayOf(BackupHelper.JSON_MIME))
                            }
                        ) {
                            Text(stringResource(R.string.import_favorites))
                        }
                        FilledTonalButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                createFavoritesBackup.launch("wall_you_favorites_backup.json")
                            }
                        ) {
                            Text(stringResource(R.string.export_favorites))
                        }
                    }
                }
            }
        }
    }
}
