package com.bnyro.wallpaper.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bnyro.wallpaper.db.DatabaseHolder
import com.bnyro.wallpaper.enums.WallpaperSource
import com.bnyro.wallpaper.ext.awaitQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BackgroundWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(applicationContext, workerParameters) {
    override suspend fun doWork(): Result {
        val bitmap = when (Preferences.getChangerSource()) {
            WallpaperSource.ONLINE -> getOnlineWallpaper()
            WallpaperSource.FAVORITES -> getFavoritesWallpaper()
            WallpaperSource.LOCAL -> getLocalWallpaper()
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

    private suspend fun getOnlineWallpaper(): Bitmap? {
        return withContext(Dispatchers.IO) {
            val url = runCatching {
                Preferences.getWallpaperChangerApi().getRandomWallpaperUrl()
            }.getOrNull() ?: return@withContext null
            ImageHelper.getSuspend(applicationContext, url, true)
        }
    }

    private suspend fun getFavoritesWallpaper(): Bitmap? {
        val favoriteUrl = awaitQuery {
            DatabaseHolder.Database.favoritesDao().getAll()
        }.random().imgSrc
        return ImageHelper.getSuspend(applicationContext, favoriteUrl, true)
    }

    private fun getLocalWallpaper(): Bitmap? {
        return runCatching {
            val wallpaperDir = applicationContext.filesDir
            val randomFile = wallpaperDir.listFiles().orEmpty().toList().shuffled().firstOrNull() ?: return null
            ImageHelper.getLocalImage(applicationContext, Uri.fromFile(randomFile))
        }.getOrNull()
    }
}
