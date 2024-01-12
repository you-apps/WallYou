package com.bnyro.wallpaper.util

import android.app.WallpaperManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.bnyro.wallpaper.enums.ResizeMethod
import com.bnyro.wallpaper.enums.WallpaperTarget
import kotlin.math.absoluteValue

object WallpaperHelper {
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setWallpaperUp(context: Context, imageBitmap: Bitmap, mode: Int) {
        val (width, height) = getMetrics(context)
        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperManager.suggestDesiredDimensions(width, height)

        if (!wallpaperManager.isWallpaperSupported) return
        wallpaperManager.setBitmap(imageBitmap, null, true, mode)
    }

    private fun setWallpaperLegacy(context: Context, imageBitmap: Bitmap) {
        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperManager.setBitmap(imageBitmap)
    }

    fun setWallpaper(context: Context, bitmap: Bitmap, mode: WallpaperTarget) {
        Thread {
            val resizeMethod = Preferences.getString(
                Preferences.resizeMethodKey,
                ResizeMethod.ZOOM.name
            ).let { ResizeMethod.valueOf(it) }
            val resizedBitmap = processBitmapByResizeMethod(context, bitmap, resizeMethod)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (mode in listOf(WallpaperTarget.BOTH, WallpaperTarget.HOME)) {
                    setWallpaperUp(context, resizedBitmap, WallpaperManager.FLAG_SYSTEM)
                }
                if (mode in listOf(WallpaperTarget.BOTH, WallpaperTarget.LOCK)) {
                    setWallpaperUp(context, resizedBitmap, WallpaperManager.FLAG_LOCK)
                }
            } else {
                setWallpaperLegacy(context, resizedBitmap)
            }
        }.start()
    }

    private fun getMetrics(context: Context): Pair<Int, Int> {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds.let { it.width() to it.height() }
        } else {
            val metrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(metrics)
            metrics.widthPixels to metrics.heightPixels
        }
    }

    private fun processBitmapByResizeMethod(context: Context, bitmap: Bitmap, resizeMethod: ResizeMethod): Bitmap {
        val (width, height) = getMetrics(context)

        return when (resizeMethod) {
            ResizeMethod.CROP -> getResizedBitmap(bitmap, width, height)
            ResizeMethod.ZOOM -> getZoomedBitmap(bitmap, width, height)
            ResizeMethod.FIT_WIDTH -> getBitmapFitWidth(bitmap, width)
            ResizeMethod.FIT_HEIGHT -> getBitmapFitHeight(bitmap, height)
            ResizeMethod.NONE -> bitmap
        }
    }

    private fun getResizedBitmap(bitmap: Bitmap, width: Int, height: Int, filter: Boolean = true): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, filter)
    }

    private fun getZoomedBitmap(bitmap: Bitmap, screenWidth: Int, screenHeight: Int): Bitmap {
        val bitmapRatio = bitmap.height.toFloat() / bitmap.width.toFloat()
        val screenRatio = screenHeight.toFloat() / screenWidth.toFloat()
        val scaleRatio = (bitmapRatio / screenRatio)

        var (newWidth, newHeight) = bitmap.width to bitmap.height
        if (bitmapRatio > screenRatio) {
            newHeight = (scaleRatio * bitmap.height).toInt()
        } else {
            newWidth = (scaleRatio * bitmap.width).toInt()
        }

        val gapX = (bitmap.width - newWidth) / 2
        val gapY = (bitmap.height - newHeight) / 2
        val centeredBitmap = Bitmap.createBitmap(bitmap, gapX, gapY, newWidth, newHeight)

        return getResizedBitmap(centeredBitmap, screenWidth, screenHeight)
    }

    private fun getBitmapFitWidth(bitmap: Bitmap, width: Int): Bitmap {
        val heightRatio = width.toFloat() / bitmap.width.toFloat()

        return getResizedBitmap(bitmap, width, (bitmap.height * heightRatio).toInt())
    }

    private fun getBitmapFitHeight(bitmap: Bitmap, height: Int): Bitmap {
        val widthRatio = height.toFloat() / bitmap.height.toFloat()

        return getResizedBitmap(bitmap, (bitmap.width * widthRatio).toInt(), height)
    }
}
