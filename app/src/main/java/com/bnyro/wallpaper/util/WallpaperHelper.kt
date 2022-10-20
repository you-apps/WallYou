package com.bnyro.wallpaper.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi

object WallpaperHelper {
    @RequiresApi(Build.VERSION_CODES.N)
    fun setWallpaper(context: Context, imageBitmap: Bitmap, mode: Int) {
        val screenWidth = context.resources.displayMetrics.widthPixels
        val screenHeight = context.resources.displayMetrics.heightPixels

        val wallpaperManager = WallpaperManager.getInstance(context)

        wallpaperManager.suggestDesiredDimensions(screenWidth, screenHeight)

        if (!wallpaperManager.isWallpaperSupported) return

        wallpaperManager.setBitmap(imageBitmap, null, true, mode)
    }

    fun setWallpaperLegacy(context: Context, imageBitmap: Bitmap) {
        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperManager.setBitmap(imageBitmap)
    }

    fun getResizedBitmap(bitmap: Bitmap): Bitmap {
        return Bitmap.createBitmap(bitmap)
    }
}
