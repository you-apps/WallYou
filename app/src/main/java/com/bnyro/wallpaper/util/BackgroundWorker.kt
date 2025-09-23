package com.bnyro.wallpaper.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bnyro.wallpaper.db.DatabaseHolder
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.enums.WallpaperSource
import com.bnyro.wallpaper.obj.WallpaperConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Random

class BackgroundWorker(
    applicationContext: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(applicationContext, workerParameters) {
    override suspend fun doWork(): Result {
        val wallpaperConfigs = Preferences.getWallpaperConfigs()

        val configId = workerParameters.inputData.getInt(WorkerHelper.WALLPAPER_CONFIG_ID, -1)
        if (configId == -1) {
            for (config in wallpaperConfigs) {
                runWallpaperChanger(config)
            }
            return Result.success()
        }

        val config = wallpaperConfigs.firstOrNull {
            it.id == configId
        } ?: return Result.success()

        val nowMillis = TimeHelper.timeTodayInMillis()
        if (config.startTimeMillis != null && config.endTimeMillis != null &&
            !TimeHelper.isInTimeRange(nowMillis, config.startTimeMillis!!, config.endTimeMillis!!)
        ) return Result.success()

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
            val url = try {
                Preferences.getApiByRoute(source).getRandomWallpaperUrl()
            } catch (e: Exception) {
                Log.e(this@BackgroundWorker::class.simpleName, e.toString())
                return@withContext null
            } ?: return@withContext null

            val bitmap = ImageHelper.urlToBitmap(url, applicationContext, forceReload = true)
            if (bitmap != null && Preferences.getBoolean(Preferences.wallpaperHistory, true)) {
                val wallpaper = Wallpaper(imgSrc = url)
                DatabaseHolder.Database.favoritesDao().insert(wallpaper, null, true)
            }

            bitmap
        }
    }

    private suspend fun getFavoritesWallpaper(): Bitmap? {
        val favoriteUrl = withContext(Dispatchers.IO) {
            DatabaseHolder.Database.favoritesDao().getRandomFavorite()
        }?.imgSrc
        return ImageHelper.urlToBitmap(favoriteUrl, applicationContext, forceReload = true)
    }

    private fun getLocalWallpaper(config: WallpaperConfig): Bitmap? {
        return try {
            val wallpapers = LocalWallpaperHelper.getLocalWalls(applicationContext, config)
            if (wallpapers.isEmpty()) return null

            // use Java's random number generator with a custom seed instead of Kotlin's
            // see https://stackoverflow.com/questions/73475522/kotlin-random-always-generates-the-same-random-numbers
            val randomGenerator = Random(System.currentTimeMillis())
            val randomIndex = randomGenerator.nextInt(wallpapers.size)

            ImageHelper.getLocalImage(applicationContext, wallpapers[randomIndex].uri)
        } catch (e: Exception) {
            Log.e(this@BackgroundWorker::class.simpleName, e.toString())
            null
        }
    }
}
