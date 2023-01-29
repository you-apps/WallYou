package com.bnyro.wallpaper.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class BackgroundWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters
) : Worker(applicationContext, workerParameters) {
    override fun doWork(): Result {
        val bitmap = if (Preferences.getBoolean(Preferences.autoChangerLocal, false)) {
            getLocalWallpaper()
        } else {
            getOnlineWallpaper()
        } ?: return Result.failure()

        WallpaperHelper.setWallpaper(
            applicationContext,
            BitmapProcessor.processBitmapByPrefs(bitmap),
            Preferences.getString(
                Preferences.wallpaperChangerTargetKey,
                Preferences.defaultWallpaperChangerTarget.toString()
            )?.toInt() ?: Preferences.defaultWallpaperChangerTarget
        )

        return Result.success()
    }

    private fun getOnlineWallpaper(): Bitmap? {
        return runBlocking(Dispatchers.IO) {
            val url = try {
                Preferences.getWallpaperChangerApi().getRandomWallpaperUrl()
            } catch (e: Exception) {
                return@runBlocking null
            }

            return@runBlocking ImageHelper.getBlocking(applicationContext, url, true)
        }
    }

    private fun getLocalWallpaper(): Bitmap? {
        return runCatching {
            val wallpaperDir = applicationContext.filesDir
            val randomFile = wallpaperDir.listFiles().orEmpty().toList().shuffled().firstOrNull() ?: return null
            ImageHelper.getLocalImage(applicationContext, Uri.fromFile(randomFile))
        }.getOrNull()
    }
}
