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

    private fun getWorkerConstraints(): Constraints {
        val wallpaperConfigs = Preferences.getWallpaperConfigs()
        // only require internet when the source is not local
        val networkType = if (wallpaperConfigs.any { it.source != WallpaperSource.LOCAL }) {
            NetworkType.CONNECTED
        } else {
            NetworkType.NOT_REQUIRED
        }

        return Constraints.Builder()
            .setRequiredNetworkType(networkType)
            .build()
    }

    fun enqueue(context: Context, forceUpdate: Boolean = false) {
        if (!Preferences.getBoolean(Preferences.wallpaperChangerKey, false)) {
            WorkManager.getInstance(context)
                .cancelUniqueWork(JOB_NAME)
            return
        }

        val repeatIntervalMinutes = Preferences.getString(
            Preferences.wallpaperChangerIntervalKey,
            Preferences.defaultWallpaperChangeInterval.toString()
        ).toLong()
        val job = PeriodicWorkRequestBuilder<BackgroundWorker>(
            repeatIntervalMinutes,
            TimeUnit.MINUTES
        )
            .setConstraints(getWorkerConstraints())
            .build()

        val policy = if (forceUpdate) ExistingPeriodicWorkPolicy.UPDATE else ExistingPeriodicWorkPolicy.KEEP
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(JOB_NAME, policy, job)
    }
}
