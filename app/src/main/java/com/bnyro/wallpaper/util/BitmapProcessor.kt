package com.bnyro.wallpaper.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.google.android.renderscript.Toolkit

object BitmapProcessor {

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

        var bitmap = bitmap.blur(blurRadius)
        bitmap = changeBitmapContrast(bitmap, contrast)
        if (grayScale) bitmap = bitmap.grayScale()

        return bitmap
    }

    private fun changeBitmapContrast(bitmap: Bitmap, contrast: Float): Bitmap {
        if (contrast == 1f) return bitmap

        val colorMatrix = ColorMatrix(
            floatArrayOf(
                contrast, 0f, 0f, 0f, 1f,
                0f, contrast, 0f, 0f, 1f,
                0f, 0f, contrast, 0f, 1f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        val ret = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(ret)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(colorMatrix)
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return ret
    }

    fun calculateBrightnessEstimate(bitmap: Bitmap, pixelSpacing: Int): Int {
        var red = 0
        var green = 0
        var blue = 0
        val height = bitmap.height
        val width = bitmap.width
        var n = 0
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        var i = 0
        while (i < pixels.size) {
            val color = pixels[i]
            red += Color.red(color)
            green += Color.green(color)
            blue += Color.blue(color)
            n++
            i += pixelSpacing
        }
        return (red + blue + green) / (n * 3)
    }

    fun invert(src: Bitmap): Bitmap {
        val height = src.height
        val width = src.width
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        val matrixGrayscale = ColorMatrix()
        matrixGrayscale.setSaturation(0f)
        val matrixInvert = ColorMatrix(
            floatArrayOf(
                -1.0f, 0.0f, 0.0f, 0.0f, 255.0f,
                0.0f, -1.0f, 0.0f, 0.0f, 255.0f,
                0.0f, 0.0f, -1.0f, 0.0f, 255.0f,
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f
            )
        )
        matrixInvert.preConcat(matrixGrayscale)
        val filter = ColorMatrixColorFilter(matrixInvert)
        paint.setColorFilter(filter)
        canvas.drawBitmap(src, 0f, 0f, paint)
        return bitmap
    }
}
