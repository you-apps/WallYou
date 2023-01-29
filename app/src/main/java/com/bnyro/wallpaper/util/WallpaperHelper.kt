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
                Preferences.getBoolean(
                    Preferences.cropImagesKey,
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

    private fun getCroppedBitmap(bitmap: Bitmap, displayMetrics: DisplayMetrics): Bitmap {
        return Bitmap.createScaledBitmap(
            bitmap,
            displayMetrics.widthPixels,
            displayMetrics.heightPixels,
            true
        )
    }

    private fun getResizedBitmap(bitmap: Bitmap, displayMetrics: DisplayMetrics): Bitmap {
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val bitmapWidth = bitmap.width.toFloat()
        val bitmapHeight = bitmap.height.toFloat()

        val bitmapRatio = bitmapHeight / bitmapWidth
        val screenRatio = screenHeight / screenWidth

        val resizedBitmap = if (screenRatio > bitmapRatio) {
            getResizedBitmap(bitmap, screenWidth, (screenWidth * bitmapRatio).toInt())
        } else {
            getResizedBitmap(bitmap, (screenHeight / bitmapRatio).toInt(), screenHeight)
        }

        val bitmapGapX = ((bitmap.width - screenWidth) / 2f).toInt()
        val bitmapGapY = ((bitmap.height - screenHeight) / 2f).toInt()

        // prevent crashes due to wrong aspect ratio
        if (bitmapGapX <= 0 || bitmapGapY <= 0) return resizedBitmap

        return runCatching {
            Bitmap.createBitmap(
                resizedBitmap,
                bitmapGapX,
                bitmapGapY,
                screenWidth,
                screenHeight
            )
        }.getOrDefault(resizedBitmap)
    }

    private fun getResizedBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(
            bitmap,
            newWidth,
            newHeight,
            false
        )
    }
}
