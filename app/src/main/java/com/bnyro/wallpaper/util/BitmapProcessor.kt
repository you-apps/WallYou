package com.bnyro.wallpaper.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.android.renderscript.Toolkit
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object BitmapProcessor {
    private val matrixInvert = floatArrayOf(
        -1f, 0f, 0f, 1.0f,
        0f, -1f, 0f, 1.0f,
        0f, 0f, -1f, 1.0f,
        1.0f, 1.0f, 1.0f, 1.0f
    )

    private fun getContrastMatrix(contrast: Float) = floatArrayOf(
        contrast, 0f, 0f, (1f - contrast) / 2,
        0f, contrast, 0f, (1f - contrast) / 2,
        0f, 0f, contrast, (1f - contrast) / 2,
        0f, 0f, 0f, 1f
    )

    private fun getBrightnessMatrix(brightness: Float) = floatArrayOf(
        brightness, 0f, 0f, 0f,
        0f, brightness, 0f, 0f,
        0f, 0f, brightness, 0f,
        0f, 0f, 0f, 1f
    )

    private fun getHueMatrix(hueAngle: Float): FloatArray {
        val cosOnly = (1 + 2 * cos(hueAngle)) / 3
        val cosSubSin = (1 - cos(hueAngle)) / 3 - sin(hueAngle) / sqrt(3f)
        val cosAddSin = (1 - cos(hueAngle)) / 3 + sin(hueAngle) / sqrt(3f)

        return floatArrayOf(
            cosOnly, cosSubSin, cosAddSin, 0f,
            cosAddSin, cosOnly, cosSubSin, 0f,
            cosSubSin, cosAddSin, cosOnly, 0f,
            0f, 0f, 0f, 1f
        )
    }

    private fun Bitmap.blur(radius: Int): Bitmap {
        if (radius == 0) return this

        return Toolkit.blur(this, radius)
    }

    fun changeBrightness(bitmap: Bitmap, brightness: Float): Bitmap {
        val brightnessMatrix = getBrightnessMatrix(brightness)
        return Toolkit.colorMatrix(bitmap, brightnessMatrix)
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

    fun getTransformMatrix(contrast: Float, brightness: Float, hue: Float, grayScale: Boolean, invert: Boolean): FloatArray {
        var transformMatrix = Toolkit.identityMatrix
        if (contrast != 1f) {
            transformMatrix = multiply(transformMatrix, getContrastMatrix(contrast))
        }
        if (brightness != 1f) {
            transformMatrix = multiply(transformMatrix, getBrightnessMatrix(brightness))
        }
        if (hue != 1f) {
            transformMatrix = multiply(transformMatrix, getHueMatrix((hue * 2 * Math.PI).toFloat()))
        }
        if (invert) {
            transformMatrix = multiply(transformMatrix, matrixInvert)
        }
        if (grayScale) {
            transformMatrix = multiply(transformMatrix, Toolkit.greyScaleColorMatrix)
        }

        return transformMatrix
    }

    fun processBitmapByPrefs(bitmap: Bitmap): Bitmap {
        val blurRadius = Preferences.getFloat(Preferences.blurKey, 0f).toInt()
        val contrast = Preferences.getFloat(Preferences.contrastKey, 1f)
        val brightness = Preferences.getFloat(Preferences.brightnessKey, 1f)
        val hue = Preferences.getFloat(Preferences.hueKey, 1f)
        val grayScale = Preferences.getBoolean(Preferences.grayscaleKey, false)
        val invert = Preferences.getBoolean(Preferences.invertKey, false)

        val transformMatrix = getTransformMatrix(contrast, brightness, hue, grayScale, invert)
        return Toolkit.colorMatrix(bitmap.blur(blurRadius), transformMatrix)
    }

    fun calculateBrightnessEstimate(bitmap: Bitmap, pixelSpacing: Int = 20): Float {
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixelsRead = 0
        var totalBrightness = 0
        for (i in pixels.indices step pixelSpacing) {
            val color = pixels[i]
            totalBrightness += Color.red(color) + Color.green(color) + Color.blue(color)
            pixelsRead++
        }

        return totalBrightness.toFloat() / (pixelsRead * 3) / 256
    }
}
