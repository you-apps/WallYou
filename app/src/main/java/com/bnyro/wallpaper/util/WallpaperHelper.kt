package com.bnyro.wallpaper.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.bnyro.wallpaper.enums.ResizeMethod
import com.bnyro.wallpaper.enums.WallpaperTarget
import kotlin.math.absoluteValue

object WallpaperHelper {
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setWallpaperUp(context: Context, imageBitmap: Bitmap, mode: Int) {
        val metrics = context.resources.displayMetrics
        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperManager.suggestDesiredDimensions(metrics.widthPixels, metrics.heightPixels)

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

    private fun processBitmapByResizeMethod(context: Context, bitmap: Bitmap, resizeMethod: ResizeMethod): Bitmap {
        val metrics = context.resources.displayMetrics

        return when (resizeMethod) {
            ResizeMethod.CROP -> getResizedBitmap(bitmap, metrics.widthPixels, metrics.heightPixels)
            ResizeMethod.ZOOM -> getZoomedBitmap(bitmap, metrics.widthPixels, metrics.heightPixels)
            ResizeMethod.FIT_WIDTH -> getBitmapFitWidth(bitmap, metrics.widthPixels)
            ResizeMethod.FIT_HEIGHT -> getBitmapFitHeight(bitmap, metrics.heightPixels)
            ResizeMethod.NONE -> bitmap
        }
    }

    private fun getResizedBitmap(bitmap: Bitmap, width: Int, height: Int, filter: Boolean = true): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, filter)
    }

    private fun getZoomedBitmap(bitmap: Bitmap, screenWidth: Int, screenHeight: Int): Bitmap {
        val bitmapRatio = bitmap.height.toFloat() / bitmap.width.toFloat()
        val screenRatio = screenHeight.toFloat() / screenWidth.toFloat()

        val resizedBitmap = if (screenRatio > bitmapRatio) {
            getResizedBitmap(bitmap, screenWidth, (screenWidth * bitmapRatio).toInt(), false)
        } else {
            getResizedBitmap(bitmap, (screenHeight / bitmapRatio).toInt(), screenHeight, false)
        }

        val bitmapGapX = ((resizedBitmap.width - screenWidth) / 2).absoluteValue
        val bitmapGapY = ((resizedBitmap.height - screenHeight) / 2).absoluteValue

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

    private fun getBitmapFitWidth(bitmap: Bitmap, width: Int): Bitmap {
        val heightRatio = width.toFloat() / bitmap.width.toFloat()

        return getResizedBitmap(bitmap, width, (bitmap.height * heightRatio).toInt())
    }

    private fun getBitmapFitHeight(bitmap: Bitmap, height: Int): Bitmap {
        val widthRatio = height.toFloat() / bitmap.height.toFloat()

        return getResizedBitmap(bitmap, (bitmap.width * widthRatio).toInt(), height)
    }
}
