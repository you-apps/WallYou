package com.bnyro.wallpaper.ui.activities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.ext.formatBinarySize
import com.bnyro.wallpaper.ext.formatMinutes
import com.bnyro.wallpaper.ui.components.about.AboutContainer
import com.bnyro.wallpaper.ui.components.prefs.CheckboxPref
import com.bnyro.wallpaper.ui.components.prefs.ListPreference
import com.bnyro.wallpaper.ui.components.prefs.SettingsCategory
import com.bnyro.wallpaper.util.PrefHolder
import com.bnyro.wallpaper.util.WorkerHelper

@Composable
fun SettingsPage() {
    val context = LocalContext.current.applicationContext

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AboutContainer {
            Column {
                SettingsCategory(
                    title = stringResource(R.string.general)
                )
                CheckboxPref(
                    prefKey = PrefHolder.cropImagesKey,
                    title = stringResource(R.string.crop_images)
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
                    prefKey = PrefHolder.diskCacheKey,
                    title = stringResource(R.string.coil_cache),
                    entries = cacheSizes.map { it.formatBinarySize() },
                    values = cacheSizes.map { it.toString() }
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
            CheckboxPref(
                prefKey = PrefHolder.wallpaperChangerKey,
                title = stringResource(R.string.wallpaper_changer)
            ) {
                if (it) {
                    WorkerHelper.enqueue(context, true)
                } else {
                    WorkerHelper.cancel(context)
                }
            }
            ListPreference(
                prefKey = PrefHolder.wallpaperChangerIntervalKey,
                title = stringResource(R.string.change_interval),
                entries = changeIntervals.map { it.formatMinutes() },
                values = changeIntervals.map { it.toString() }
            ) {
                WorkerHelper.enqueue(context, true)
            }
            ListPreference(
                prefKey = PrefHolder.wallpaperChangerTargetKey,
                title = stringResource(R.string.change_target),
                entries = listOf(
                    stringResource(R.string.both),
                    stringResource(R.string.home),
                    stringResource(R.string.lockscreen)
                ),
                values = (0..2).map { it.toString() }
            )
        }
    }
}
