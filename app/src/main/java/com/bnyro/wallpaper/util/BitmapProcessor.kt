package com.bnyro.wallpaper.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.android.renderscript.Toolkit

object BitmapProcessor {
    private val matrixInvert = floatArrayOf(
        -1f, 0f, 0f, 1.0f,
        0f, -1f, 0f, 1.0f,
        0f, 0f, -1f, 1.0f,
        1.0f, 1.0f, 1.0f, 1.0f
    )

    private fun Bitmap.blur(radius: Int): Bitmap {
        if (radius == 0) return this

        return Toolkit.blur(this, radius)
    }

    private fun Bitmap.grayScale(): Bitmap {
        return Toolkit.colorMatrix(this, Toolkit.greyScaleColorMatrix)
    }

    fun processBitmapByPrefs(bitmap: Bitmap): Bitmap {
        val blurRadius = Preferences.getFloat(Preferences.blurKey, 0f).toInt()
        val contrast = Preferences.getFloat(Preferences.contrastKey, 1f)
        val grayScale = Preferences.getBoolean(Preferences.grayscaleKey, false)

        var bm = bitmap.blur(blurRadius)
        bm = changeBitmapContrast(bm, contrast)
        if (grayScale) bm = bm.grayScale()

        return bm
    }

    private fun changeBitmapContrast(bitmap: Bitmap, contrast: Float): Bitmap {
        if (contrast == 1f) return bitmap

        val colorMatrix = floatArrayOf(
            contrast, 0f, 0f, 0f,
            0f, contrast, 0f, 0f,
            0f, 0f, contrast, 0f,
            0f, 0f, 0f, 1f
        )

        return Toolkit.colorMatrix(bitmap, colorMatrix)
    }

    fun calculateBrightnessEstimate(bitmap: Bitmap, pixelSpacing: Int): Int {
        val height = bitmap.height
        val width = bitmap.width

        var pixelsRead = 0
        var red = 0
        var green = 0
        var blue = 0

        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        for (i in pixels.indices step pixelSpacing) {
            val color = pixels[i]
            red += Color.red(color)
            green += Color.green(color)
            blue += Color.blue(color)
            pixelsRead++
        }

        return (red + blue + green) / (pixelsRead * 3)
    }

    fun invert(src: Bitmap): Bitmap {
        return Toolkit.colorMatrix(src, matrixInvert)
    }
}
