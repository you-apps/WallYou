package com.bnyro.wallpaper.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.DisplayMetrics
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
            val resizedBitmap = if (
                PrefHolder.Preferences.getBoolean(
                    PrefHolder.cropImagesKey,
                    false
                )
            ) {
                getCroppedBitmap(bitmap, context.resources.displayMetrics)
            } else {
                getResizedBitmap(bitmap, context.resources.displayMetrics)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (mode in listOf(WallpaperMode.BOTH, WallpaperMode.HOME)) {
                    setWallpaperUp(context, resizedBitmap, WallpaperManager.FLAG_SYSTEM)
                }
                if (mode in listOf(WallpaperMode.BOTH, WallpaperMode.LOCK)) {
                    setWallpaperUp(context, resizedBitmap, WallpaperManager.FLAG_LOCK)
                }
            } else {
                setWallpaperLegacy(context, resizedBitmap)
            }
        }.start()
    }

    fun getCroppedBitmap(bitmap: Bitmap, displayMetrics: DisplayMetrics): Bitmap {
        return Bitmap.createScaledBitmap(
            bitmap,
            displayMetrics.widthPixels,
            displayMetrics.heightPixels,
            true
        )
    }

    fun getResizedBitmap(bitmap: Bitmap, displayMetrics: DisplayMetrics): Bitmap {
        var resizedBitmap = bitmap
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val bitmapWidth = resizedBitmap.width.toFloat()
        val bitmapHeight = resizedBitmap.height.toFloat()

        val bitmapRatio = (bitmapWidth / bitmapHeight)
        val screenRatio = (screenWidth / screenHeight)
        val bitmapNewWidth: Int
        val bitmapNewHeight: Int

        if (screenRatio > bitmapRatio) {
            bitmapNewWidth = screenWidth
            bitmapNewHeight = (bitmapNewWidth / bitmapRatio).toInt()
        } else {
            bitmapNewHeight = screenHeight
            bitmapNewWidth = (bitmapNewHeight * bitmapRatio).toInt()
        }

        resizedBitmap = Bitmap.createScaledBitmap(
            resizedBitmap,
            bitmapNewWidth,
            bitmapNewHeight,
            false
        )

        val bitmapGapX: Int = ((bitmapNewWidth - screenWidth) / 2.0f).toInt()
        val bitmapGapY: Int = ((bitmapNewHeight - screenHeight) / 2.0f).toInt()

        return Bitmap.createBitmap(
            resizedBitmap,
            bitmapGapX,
            bitmapGapY,
            screenWidth,
            screenHeight
        )
    }
}
