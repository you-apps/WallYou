package com.bnyro.wallpaper.util

import android.content.Context
import android.graphics.Bitmap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bnyro.wallpaper.db.DatabaseHolder
import com.bnyro.wallpaper.enums.WallpaperConfig
import com.bnyro.wallpaper.enums.WallpaperSource
import com.bnyro.wallpaper.ext.awaitQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BackgroundWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(applicationContext, workerParameters) {
    override suspend fun doWork(): Result {
        Preferences.getWallpaperConfigs().forEach {
            runWallpaperChanger(it)
        }

        return Result.success()
    }

    private suspend fun runWallpaperChanger(config: WallpaperConfig) {
        val bitmap = when (config.source) {
            WallpaperSource.ONLINE -> getOnlineWallpaper(config)
            WallpaperSource.FAVORITES -> getFavoritesWallpaper()
            WallpaperSource.LOCAL -> getLocalWallpaper(config)
            else -> return
        } ?: return

        WallpaperHelper.setWallpaper(
            applicationContext,
            BitmapProcessor.processBitmapByPrefs(bitmap),
            config.target
        )
    }

    private suspend fun getOnlineWallpaper(config: WallpaperConfig): Bitmap? {
        return withContext(Dispatchers.IO) {
            val url = runCatching {
                Preferences.getApiByRoute(config.apiRoute.orEmpty()).getRandomWallpaperUrl()
            }.getOrElse { return@withContext null }
            ImageHelper.getSuspend(applicationContext, url, true)
        }
    }

    private suspend fun getFavoritesWallpaper(): Bitmap? {
        val favoriteUrl = awaitQuery {
            DatabaseHolder.Database.favoritesDao().getAll()
        }.randomOrNull()?.imgSrc
        return ImageHelper.getSuspend(applicationContext, favoriteUrl, true)
    }

    private fun getLocalWallpaper(config: WallpaperConfig): Bitmap? {
        return runCatching {
            val wallpaper = LocalWallpaperHelper.getLocalWalls(applicationContext, config)
                .randomOrNull() ?: return null
            ImageHelper.getLocalImage(applicationContext, wallpaper.uri)
        }.getOrNull()
    }
}
