package com.bnyro.wallpaper.util

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bnyro.wallpaper.enums.WallpaperSource
import java.util.concurrent.TimeUnit

object WorkerHelper {
    private const val JOB_NAME = "WallpaperChanger"

    fun enqueue(context: Context, forceUpdate: Boolean = false) {
        if (!Preferences.getBoolean(Preferences.wallpaperChangerKey, false)) {
            cancel(context)
            return
        }

        val networkType = if (
            Preferences.getWallpaperConfigs().any { it.source != WallpaperSource.LOCAL }
        ) {
            NetworkType.NOT_REQUIRED
        } else {
            NetworkType.CONNECTED
        }

        val job = PeriodicWorkRequestBuilder<BackgroundWorker>(
            Preferences.getString(
                Preferences.wallpaperChangerIntervalKey,
                Preferences.defaultWallpaperChangeInterval.toString()
            )?.toLong() ?: Preferences.defaultWallpaperChangeInterval,
            TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(networkType)
                .build()
        ).build()

        val policy = if (forceUpdate) ExistingPeriodicWorkPolicy.UPDATE else ExistingPeriodicWorkPolicy.KEEP
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(JOB_NAME, policy, job)
    }

    private fun cancel(context: Context) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(JOB_NAME)
    }
}
