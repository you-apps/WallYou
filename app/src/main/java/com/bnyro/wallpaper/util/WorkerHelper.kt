package com.bnyro.wallpaper.util

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bnyro.wallpaper.enums.WallpaperSource
import com.bnyro.wallpaper.obj.WallpaperConfig
import java.util.concurrent.TimeUnit

object WorkerHelper {
    private const val JOB_NAME_PREFIX = "WallpaperChanger"
    const val WALLPAPER_CONFIG_ID = "WallpaperConfigId"

    private fun getWorkerConstraints(config: WallpaperConfig): Constraints {
        // only require internet when the source is not local
        val networkType = if (config.source != WallpaperSource.LOCAL) {
            config.networkType
        } else {
            NetworkType.NOT_REQUIRED
        }

        return Constraints.Builder()
            .setRequiredNetworkType(networkType)
            .build()
    }

    private fun getWorkName(config: WallpaperConfig): String {
        return JOB_NAME_PREFIX + "_" + config.id.toString()
    }

    fun cancelWork(context: Context, config: WallpaperConfig) {
        val jobName = getWorkName(config)

        WorkManager.getInstance(context)
            .cancelUniqueWork(jobName)
    }

    fun enqueue(context: Context, config: WallpaperConfig, forceUpdate: Boolean = false) {
        val jobName = getWorkName(config)
        val inputData = Data.Builder()
            .putInt(WALLPAPER_CONFIG_ID, config.id)
            .build()

        val job = PeriodicWorkRequestBuilder<BackgroundWorker>(
            config.changeIntervalMinutes.toLong(),
            TimeUnit.MINUTES
        )
            .setConstraints(getWorkerConstraints(config))
            .setInputData(inputData)
            .build()

        val policy =
            if (forceUpdate) ExistingPeriodicWorkPolicy.UPDATE else ExistingPeriodicWorkPolicy.KEEP
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(jobName, policy, job)
    }

    fun enqueueOrCancelAll(context: Context, configs: List<WallpaperConfig>, forceUpdate: Boolean = false) {
        if (Preferences.getBoolean(Preferences.wallpaperChangerKey, false)) {
            for (config in configs) {
                enqueue(context, config, forceUpdate)
            }
        } else {
            for (config in configs) {
                cancelWork(context, config)
            }
        }
    }
}
