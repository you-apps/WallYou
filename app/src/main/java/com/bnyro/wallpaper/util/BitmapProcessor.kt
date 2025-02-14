package com.bnyro.wallpaper.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.android.renderscript.Toolkit
import kotlin.math.sqrt

object BitmapProcessor {
    val matrixInvert = floatArrayOf(
        -1f, 0f, 0f, 1.0f,
        0f, -1f, 0f, 1.0f,
        0f, 0f, -1f, 1.0f,
        1.0f, 1.0f, 1.0f, 1.0f
    )

    fun getContrastMatrix(contrast: Float) = floatArrayOf(
        contrast, 0f, 0f, (1f - contrast) / 2,
        0f, contrast, 0f, (1f - contrast) / 2,
        0f, 0f, contrast, (1f - contrast) / 2,
        0f, 0f, 0f, 1f
    )

    fun getBrightnessMatrix(brightness: Float) = floatArrayOf(
        brightness, 0f, 0f, 0f,
        0f, brightness, 0f, 0f,
        0f, 0f, brightness, 0f,
        0f, 0f, 0f, 1f
    )

    private fun Bitmap.blur(radius: Int): Bitmap {
        if (radius == 0) return this

        return Toolkit.blur(this, radius)
    }

        fun multiply(a: FloatArray, b: FloatArray): FloatArray {
        val result = FloatArray(a.size)

        val dimension = sqrt(a.size.toDouble()).toInt()
        for (i in 0 until dimension) {
            for (j in 0 until dimension) {
                for (k in 0 until dimension) {
                    result[i * dimension + j] += a[i * dimension + k] * b[k * dimension + j]
                }
            }
        }

        return result
    }

    fun getTransformMatrix(contrast: Float, brightness: Float, grayScale: Boolean, invert: Boolean = false): FloatArray {
        var transformMatrix = Toolkit.identityMatrix
        if (contrast != 1f) {
            transformMatrix = multiply(transformMatrix, getContrastMatrix(contrast))
        }
        if (brightness != 1f) {
            transformMatrix = multiply(transformMatrix, getBrightnessMatrix(brightness))
        }
        if (grayScale) {
            transformMatrix = multiply(transformMatrix, Toolkit.greyScaleColorMatrix)
        }
        if (invert) {
            transformMatrix = multiply(transformMatrix, matrixInvert)
        }

        return transformMatrix
    }

    fun processBitmapByPrefs(bitmap: Bitmap): Bitmap {
        val blurRadius = Preferences.getFloat(Preferences.blurKey, 0f).toInt()
        val contrast = Preferences.getFloat(Preferences.contrastKey, 1f)
        val brightness = Preferences.getFloat(Preferences.brightnessKey, 1f)
        val grayScale = Preferences.getBoolean(Preferences.grayscaleKey, false)

        val transformMatrix = getTransformMatrix(contrast, brightness, grayScale)
        return Toolkit.colorMatrix(bitmap.blur(blurRadius), transformMatrix)
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
