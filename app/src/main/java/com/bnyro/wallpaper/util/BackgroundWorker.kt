package com.bnyro.wallpaper.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bnyro.wallpaper.db.DatabaseHolder
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.obj.WallpaperConfig
import com.bnyro.wallpaper.enums.WallpaperSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BackgroundWorker(
    applicationContext: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(applicationContext, workerParameters) {
    override suspend fun doWork(): Result {
        val configId = workerParameters.inputData.getInt(WorkerHelper.WALLPAPER_CONFIG_ID, -1)
        if (configId == -1) return Result.failure()

        val config = Preferences.getWallpaperConfigs().firstOrNull {
            it.id == configId
        } ?: return Result.success()

        Log.e("wallpaper changer", "found appropriate wallpaper config")
        return if (runWallpaperChanger(config)) Result.success()
        else Result.retry()
    }

    /**
     * Fetch and apply a wallpaper using the given config
     *
     * @return Whether the wallpaper change applied successfully without errors
     */
    private suspend fun runWallpaperChanger(config: WallpaperConfig): Boolean {
        val bitmap = when (config.source) {
            WallpaperSource.ONLINE -> getOnlineWallpaper(config)
            WallpaperSource.FAVORITES -> getFavoritesWallpaper()
            WallpaperSource.LOCAL -> getLocalWallpaper(config)
            else -> return true
        } ?: return false

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

        return true
    }

    private suspend fun getOnlineWallpaper(config: WallpaperConfig): Bitmap? {
        val source = config.selectedApiRoutes.ifEmpty { return null }.random()

        return withContext(Dispatchers.IO) {
            val url = runCatching {
                Preferences.getApiByRoute(source).getRandomWallpaperUrl()
            }.getOrNull() ?: return@withContext null

            val bitmap = ImageHelper.getSuspend(applicationContext, url, true)
            if (bitmap != null && Preferences.getBoolean(Preferences.wallpaperHistory, true)) {
                val wallpaper = Wallpaper(imgSrc = url)
                DatabaseHolder.Database.favoritesDao().insert(wallpaper, null, true)
            }

            bitmap
        }
    }

    private suspend fun getFavoritesWallpaper(): Bitmap? {
        val favoriteUrl = withContext(Dispatchers.IO) {
            DatabaseHolder.Database.favoritesDao().getFavorites()
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
