package com.bnyro.wallpaper.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.bnyro.wallpaper.constants.WallpaperMode

object WallpaperHelper {
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setWallpaperUp(context: Context, imageBitmap: Bitmap, mode: Int) {
        val screenWidth = context.resources.displayMetrics.widthPixels
        val screenHeight = context.resources.displayMetrics.heightPixels

        val wallpaperManager = WallpaperManager.getInstance(context)

        wallpaperManager.suggestDesiredDimensions(screenWidth, screenHeight)

        if (!wallpaperManager.isWallpaperSupported) return

        wallpaperManager.setBitmap(imageBitmap, null, true, mode)
    }

    private fun setWallpaperLegacy(context: Context, imageBitmap: Bitmap) {
        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperManager.setBitmap(imageBitmap)
    }

    fun setWallpaper(context: Context, bitmap: Bitmap, mode: Int) {
        Thread {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (mode in listOf(WallpaperMode.BOTH, WallpaperMode.HOME)) {
                    setWallpaperUp(context, bitmap, WallpaperManager.FLAG_SYSTEM)
                }
                if (mode in listOf(WallpaperMode.BOTH, WallpaperMode.LOCK)) {
                    setWallpaperUp(context, bitmap, WallpaperManager.FLAG_LOCK)
                }
            } else {
                setWallpaperLegacy(context, bitmap)
            }
        }.start()
    }

    fun getResizedBitmap(bitmap: Bitmap): Bitmap {
        return Bitmap.createBitmap(bitmap)
    }
}
