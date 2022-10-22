package com.bnyro.wallpaper.util

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bnyro.wallpaper.api.wh.WhApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class BackgroundWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters
) : Worker(applicationContext, workerParameters) {
    override fun doWork(): Result {
        var success = true
        runBlocking(Dispatchers.IO) {
            val task = async {
                WhApi().getWallpapers(1)
            }
            val wallpaper = try {
                task
                    .await()
                    .shuffled()
                    .first()
            } catch (e: Exception) {
                success = false
                return@runBlocking
            }

            ImageHelper.getBlocking(applicationContext, wallpaper.imgSrc)?.let {
                WallpaperHelper.setWallpaper(
                    applicationContext,
                    it,
                    Preferences.getString(
                        Preferences.wallpaperChangerTargetKey,
                        Preferences.defaultWallpaperChangerTarget.toString()
                    )?.toInt() ?: Preferences.defaultWallpaperChangerTarget
                )
            }
        }
        return if (success) Result.success() else Result.retry()
    }
}
