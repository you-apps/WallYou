package com.bnyro.wallpaper.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

object ColorFilterGenerator {
    private val DELTA_INDEX = doubleArrayOf(
        0.0, 0.01, 0.02, 0.04, 0.05, 0.06, 0.07, 0.08, 0.1, 0.11,
        0.12, 0.14, 0.15, 0.16, 0.17, 0.18, 0.20, 0.21, 0.22, 0.24,
        0.25, 0.27, 0.28, 0.30, 0.32, 0.34, 0.36, 0.38, 0.40, 0.42,
        0.44, 0.46, 0.48, 0.5, 0.53, 0.56, 0.59, 0.62, 0.65, 0.68,
        0.71, 0.74, 0.77, 0.80, 0.83, 0.86, 0.89, 0.92, 0.95, 0.98,
        1.0, 1.06, 1.12, 1.18, 1.24, 1.30, 1.36, 1.42, 1.48, 1.54,
        1.60, 1.66, 1.72, 1.78, 1.84, 1.90, 1.96, 2.0, 2.12, 2.25,
        2.37, 2.50, 2.62, 2.75, 2.87, 3.0, 3.2, 3.4, 3.6, 3.8,
        4.0, 4.3, 4.7, 4.9, 5.0, 5.5, 6.0, 6.5, 6.8, 7.0,
        7.3, 7.5, 7.8, 8.0, 8.4, 8.7, 9.0, 9.4, 9.6, 9.8,
        10.0
    )

    /**
     * @param cm
     * @param value
     */
    fun adjustHue(cm: ColorMatrix, hue: Float) {
        var value = hue
        value = cleanValue(value, 180f) / 180f * Math.PI.toFloat()
        if (value == 0f) {
            return
        }
        val cosVal = cos(value.toDouble()).toFloat()
        val sinVal = sin(value.toDouble()).toFloat()
        val lumR = 0.213f
        val lumG = 0.715f
        val lumB = 0.072f
        val mat = floatArrayOf(
            lumR + cosVal * (1 - lumR) + sinVal * -lumR,
            lumG + cosVal * -lumG + sinVal * -lumG,
            lumB + cosVal * -lumB + sinVal * (1 - lumB), 0f, 0f,
            lumR + cosVal * -lumR + sinVal * 0.143f,
            lumG + cosVal * (1 - lumG) + sinVal * 0.140f,
            lumB + cosVal * -lumB + sinVal * -0.283f, 0f, 0f,
            lumR + cosVal * -lumR + sinVal * -(1 - lumR),
            lumG + cosVal * -lumG + sinVal * lumG,
            lumB + cosVal * (1 - lumB) + sinVal * lumB, 0f, 0f,
            0f, 0f, 0f, 1f, 0f,
            0f, 0f, 0f, 0f, 1f
        )
        cm.postConcat(ColorMatrix(mat))
    }

    fun adjustBrightness(cm: ColorMatrix, brightness: Float) {
        var value = brightness
        value = cleanValue(value, 100f)
        if (value == 0f) {
            return
        }
        val mat = floatArrayOf(
            1f, 0f, 0f, 0f, value,
            0f, 1f, 0f, 0f, value,
            0f, 0f, 1f, 0f, value,
            0f, 0f, 0f, 1f, 0f,
            0f, 0f, 0f, 0f, 1f
        )
        cm.postConcat(ColorMatrix(mat))
    }

    fun adjustContrast(cm: ColorMatrix, contrast: Int) {
        var value = contrast
        value = cleanValue(value.toFloat(), 100f).toInt()
        if (value == 0) {
            return
        }
        var x: Float
        if (value < 0) {
            x = 127 + value.toFloat() / 100 * 127
        } else {
            x = (value % 1).toFloat()
            x = if (x == 0f) {
                DELTA_INDEX[value].toFloat()
            } else {
                // x = DELTA_INDEX[(p_val<<0)]; // this is how the IDE does it.
                DELTA_INDEX[value shl 0].toFloat() * (1 - x) + DELTA_INDEX[(value shl 0) + 1].toFloat() * x // use linear interpolation for more granularity.
            }
            x = x * 127 + 127
        }
        val mat = floatArrayOf(
            x / 127, 0f, 0f, 0f, 0.5f * (127 - x),
            0f, x / 127, 0f, 0f, 0.5f * (127 - x),
            0f, 0f, x / 127, 0f, 0.5f * (127 - x),
            0f, 0f, 0f, 1f, 0f,
            0f, 0f, 0f, 0f, 1f
        )
        cm.postConcat(ColorMatrix(mat))
    }

    fun adjustSaturation(cm: ColorMatrix, saturation: Float) {
        var value = saturation
        value = cleanValue(value, 100f)
        if (value == 0f) {
            return
        }
        val x = 1 + if (value > 0) 3 * value / 100 else value / 100
        val lumR = 0.3086f
        val lumG = 0.6094f
        val lumB = 0.0820f
        val mat = floatArrayOf(
            lumR * (1 - x) + x, lumG * (1 - x), lumB * (1 - x), 0f, 0f,
            lumR * (1 - x), lumG * (1 - x) + x, lumB * (1 - x), 0f, 0f,
            lumR * (1 - x), lumG * (1 - x), lumB * (1 - x) + x, 0f, 0f,
            0f, 0f, 0f, 1f, 0f,
            0f, 0f, 0f, 0f, 1f
        )
        cm.postConcat(ColorMatrix(mat))
    }

    internal fun cleanValue(p_val: Float, p_limit: Float): Float {
        return p_limit.coerceAtMost((-p_limit).coerceAtLeast(p_val))
    }

    fun adjustColor(brightness: Int, contrast: Int, saturation: Int, hue: Int): ColorFilter {
        val cm = ColorMatrix()
        adjustHue(cm, hue.toFloat())
        adjustContrast(cm, contrast)
        adjustBrightness(cm, brightness.toFloat())
        adjustSaturation(cm, saturation.toFloat())
        return ColorMatrixColorFilter(cm)
    }

    fun blur(image: Bitmap, context: Context?, radius: Float): Bitmap? {
        val bitmapScale = 0.7f
        val width = (image.width * bitmapScale).roundToInt()
        val height = (image.height * bitmapScale).roundToInt()
        val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)
        val rs = RenderScript.create(context)
        val theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
        theIntrinsic.setRadius(radius)
        theIntrinsic.setInput(tmpIn)
        theIntrinsic.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        return outputBitmap
    }
}
