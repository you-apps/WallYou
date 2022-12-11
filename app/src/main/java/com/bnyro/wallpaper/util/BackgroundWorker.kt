package com.bnyro.wallpaper.util

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bnyro.wallpaper.api.ps.PsApi
import com.bnyro.wallpaper.api.wh.WhApi
import com.bnyro.wallpaper.ui.nav.DrawerScreens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class BackgroundWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters
) : Worker(applicationContext, workerParameters) {
    override fun doWork(): Result {
        var success = true
        runBlocking(Dispatchers.IO) {
            val url = try {
                when (Preferences.getString(Preferences.wallpaperChangerApiKey, "")) {
                    DrawerScreens.Picsum.route -> PsApi()
                    else -> WhApi()
                }.getRandomWallpaperUrl()
            } catch (e: Exception) {
                success = false
                return@runBlocking
            }

            ImageHelper.getBlocking(applicationContext, url, true)?.let {
                WallpaperHelper.setWallpaper(
                    applicationContext,
                    BitmapProcessor.processBitmapByPrefs(it),
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
