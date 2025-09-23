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
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.bnyro.wallpaper.ui.components.about.AboutContainer
import com.bnyro.wallpaper.ui.components.prefs.CheckboxPref
import com.bnyro.wallpaper.ui.components.prefs.ListPreference
import com.bnyro.wallpaper.ui.components.prefs.SettingsCategory
import com.bnyro.wallpaper.ui.models.MainModel
import com.bnyro.wallpaper.util.BackupHelper
import com.bnyro.wallpaper.util.Preferences
import com.bnyro.wallpaper.util.WorkerHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    viewModel.themeMode = ThemeMode.entries[it.toInt()]
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

        AboutContainer {
            SettingsCategory(
                title = stringResource(R.string.wallpaper_changer)
            )
            Spacer(
                modifier = Modifier
                    .height(5.dp)
            )

            var wallpaperChangerEnabled by remember {
                mutableStateOf(Preferences.getBoolean(Preferences.wallpaperChangerKey, false))
            }
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
                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = wallpaperConfig.getSummary(context))

                            Spacer(modifier = Modifier.weight(1f))

                            ButtonWithIcon(
                                icon = Icons.Default.Edit
                            ) {
                                wallpaperConfigDialogIndex = index
                            }

                            ButtonWithIcon(
                                icon = Icons.Default.Delete
                            ) {
                                WorkerHelper.cancelWork(context, wallpaperConfig)
                                wallpaperConfigs.removeAt(index)
                                Preferences.setWallpaperConfigs(wallpaperConfigs)
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

                    Button(
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
                            Spacer(Modifier.width(5.dp))
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

        AboutContainer {
            SettingsCategory(
                title = stringResource(R.string.import_export)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                Button(
                    onClick = {
                        restoreFavoritesBackup.launch(arrayOf(BackupHelper.JSON_MIME))
                    }
                ) {
                    Text(stringResource(R.string.import_favorites))
                }
                Spacer(modifier = Modifier.width(6.dp))
                Button(
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
