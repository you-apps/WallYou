package com.bnyro.wallpaper.util

import android.content.Context
import android.graphics.Bitmap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bnyro.wallpaper.db.DatabaseHolder
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.obj.WallpaperConfig
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

        if (config.applyImageFilters) {
            WallpaperHelper.setWallpaperWithFilters(
                applicationContext,
                bitmap,
                config.target
            )
        } else {
            WallpaperHelper.setWallpaperWithoutFilters(
                applicationContext,
                bitmap,
                config.target
            )
        }
    }

    private suspend fun getOnlineWallpaper(config: WallpaperConfig): Bitmap? {
        val source = config.selectedApiRoutes.ifEmpty { return null }.random()

        return withContext(Dispatchers.IO) {
            val url = runCatching {
                Preferences.getApiByRoute(source).getRandomWallpaperUrl()
            }.getOrNull() ?: return@withContext null

            val bitmap = ImageHelper.getSuspend(applicationContext, url, true)
            if (bitmap != null && Preferences.getBoolean(Preferences.autoAddToFavoritesKey, false)) {
                DatabaseHolder.Database.favoritesDao().insertAll(Wallpaper(imgSrc = url))
            }

            bitmap
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
