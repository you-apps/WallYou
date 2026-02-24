package com.bnyro.wallpaper.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.core.graphics.createBitmap
import kotlin.math.max
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object BitmapProcessor {
    private const val maxBlurRadius = 25

    private val identityMatrix = floatArrayOf(
        1f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f,
        0f, 0f, 1f, 0f,
        0f, 0f, 0f, 1f
    )

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
        val safeRadius = radius.coerceIn(0, maxBlurRadius)
        if (safeRadius == 0) return this

        return stackBlur(safeRadius)
    }

    fun changeBrightness(bitmap: Bitmap, brightness: Float): Bitmap {
        return applyColorFilterMatrix(
            bitmap = bitmap,
            transformMatrix = getBrightnessMatrix(brightness).toAndroidColorMatrix(),
            grayScale = false
        )
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

    fun getTransformMatrix(
        contrast: Float,
        brightness: Float,
        hue: Float,
        invert: Boolean
    ): FloatArray {
        var transformMatrix = identityMatrix
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

        return transformMatrix.toAndroidColorMatrix()
    }

    fun processBitmapByPrefs(bitmap: Bitmap): Bitmap {
        val blurRadius = Preferences.getFloat(Preferences.blurKey, 0f).toInt()
        val contrast = Preferences.getFloat(Preferences.contrastKey, 1f)
        val brightness = Preferences.getFloat(Preferences.brightnessKey, 1f)
        val hue = Preferences.getFloat(Preferences.hueKey, 1f)
        val grayScale = Preferences.getBoolean(Preferences.grayscaleKey, false)
        val invert = Preferences.getBoolean(Preferences.invertKey, false)

        val transformMatrix = getTransformMatrix(contrast, brightness, hue, invert)
        return applyColorFilterMatrix(bitmap, transformMatrix, grayScale).blur(blurRadius)
    }

    private fun applyColorFilterMatrix(
        bitmap: Bitmap,
        transformMatrix: FloatArray,
        grayScale: Boolean
    ): Bitmap {
        val cm = ColorMatrix(transformMatrix).apply {
            if (grayScale) {
                // grayscaling has to be done with a new matrix because it would
                // otherwise override all other transformations (contrast, hue, ...)
                val grayScaleMatrix = ColorMatrix().apply { setSaturation(0f) }
                postConcat(grayScaleMatrix)
            }
        }
        val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(cm) }

        val output = createBitmap(bitmap.width, bitmap.height)

        val canvas = Canvas(output)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return output
    }

    private fun FloatArray.toAndroidColorMatrix(): FloatArray {
        // Android expects a 4x5 matrix (20 values) for ColorMatrix operations.
        val paddedMatrix = FloatArray(20)
        var sourceIndex = 0
        for (row in 0 until 4) {
            for (column in 0 until 4) {
                paddedMatrix[row * 5 + column] = this[sourceIndex++]
            }
            paddedMatrix[row * 5 + 4] = 0f
        }
        return paddedMatrix
    }

    private fun Bitmap.stackBlur(radius: Int): Bitmap {
        val output = copy(Bitmap.Config.ARGB_8888, true)
        val width = output.width
        val height = output.height

        if (width <= 1 || height <= 1) return output

        val pixels = IntArray(width * height)
        output.getPixels(pixels, 0, width, 0, 0, width, height)

        val widthMax = width - 1
        val heightMax = height - 1
        val widthHeight = width * height
        val diameter = radius * 2 + 1

        val reds = IntArray(widthHeight)
        val greens = IntArray(widthHeight)
        val blues = IntArray(widthHeight)
        val minValues = IntArray(max(width, height))

        val divisorSum = (diameter + 1) shr 1
        val divisor = divisorSum * divisorSum
        val divisionLookup = IntArray(256 * divisor) { it / divisor }

        var yIndex = 0
        var yWidth = 0

        val stack = Array(diameter) { IntArray(3) }
        val radiusPlusOne = radius + 1

        var stackPointer: Int
        var stackStart: Int
        var rgb: IntArray
        var radiusBlurScale: Int
        var redOutSum: Int
        var greenOutSum: Int
        var blueOutSum: Int
        var redInSum: Int
        var greenInSum: Int
        var blueInSum: Int

        for (y in 0 until height) {
            var redSum = 0
            var greenSum = 0
            var blueSum = 0
            redOutSum = 0
            greenOutSum = 0
            blueOutSum = 0
            redInSum = 0
            greenInSum = 0
            blueInSum = 0

            for (i in -radius..radius) {
                rgb = stack[i + radius]
                val pixel = pixels[yWidth + i.coerceIn(0, widthMax)]
                rgb[0] = (pixel and 0x00ff0000) shr 16
                rgb[1] = (pixel and 0x0000ff00) shr 8
                rgb[2] = pixel and 0x000000ff

                radiusBlurScale = radiusPlusOne - kotlin.math.abs(i)
                redSum += rgb[0] * radiusBlurScale
                greenSum += rgb[1] * radiusBlurScale
                blueSum += rgb[2] * radiusBlurScale

                if (i > 0) {
                    redInSum += rgb[0]
                    greenInSum += rgb[1]
                    blueInSum += rgb[2]
                } else {
                    redOutSum += rgb[0]
                    greenOutSum += rgb[1]
                    blueOutSum += rgb[2]
                }
            }

            stackPointer = radius
            for (x in 0 until width) {
                reds[yIndex] = divisionLookup[redSum]
                greens[yIndex] = divisionLookup[greenSum]
                blues[yIndex] = divisionLookup[blueSum]

                redSum -= redOutSum
                greenSum -= greenOutSum
                blueSum -= blueOutSum

                stackStart = stackPointer - radius + diameter
                rgb = stack[stackStart % diameter]

                redOutSum -= rgb[0]
                greenOutSum -= rgb[1]
                blueOutSum -= rgb[2]

                if (y == 0) {
                    minValues[x] = (x + radius + 1).coerceAtMost(widthMax)
                }
                val pixel = pixels[yWidth + minValues[x]]

                rgb[0] = (pixel and 0x00ff0000) shr 16
                rgb[1] = (pixel and 0x0000ff00) shr 8
                rgb[2] = pixel and 0x000000ff

                redInSum += rgb[0]
                greenInSum += rgb[1]
                blueInSum += rgb[2]

                redSum += redInSum
                greenSum += greenInSum
                blueSum += blueInSum

                stackPointer = (stackPointer + 1) % diameter
                rgb = stack[stackPointer]

                redOutSum += rgb[0]
                greenOutSum += rgb[1]
                blueOutSum += rgb[2]

                redInSum -= rgb[0]
                greenInSum -= rgb[1]
                blueInSum -= rgb[2]

                yIndex++
            }
            yWidth += width
        }

        for (x in 0 until width) {
            var redSum = 0
            var greenSum = 0
            var blueSum = 0
            redOutSum = 0
            greenOutSum = 0
            blueOutSum = 0
            redInSum = 0
            greenInSum = 0
            blueInSum = 0

            var yOffset = -radius * width
            for (i in -radius..radius) {
                val y = yOffset.coerceAtLeast(0)
                rgb = stack[i + radius]
                rgb[0] = reds[x + y]
                rgb[1] = greens[x + y]
                rgb[2] = blues[x + y]

                radiusBlurScale = radiusPlusOne - kotlin.math.abs(i)
                redSum += reds[x + y] * radiusBlurScale
                greenSum += greens[x + y] * radiusBlurScale
                blueSum += blues[x + y] * radiusBlurScale

                if (i > 0) {
                    redInSum += rgb[0]
                    greenInSum += rgb[1]
                    blueInSum += rgb[2]
                } else {
                    redOutSum += rgb[0]
                    greenOutSum += rgb[1]
                    blueOutSum += rgb[2]
                }

                if (i < heightMax) {
                    yOffset += width
                }
            }

            var pixelIndex = x
            stackPointer = radius
            for (y in 0 until height) {
                val alpha = pixels[pixelIndex] and -0x1000000
                pixels[pixelIndex] = alpha or
                    (divisionLookup[redSum] shl 16) or
                    (divisionLookup[greenSum] shl 8) or
                    divisionLookup[blueSum]

                redSum -= redOutSum
                greenSum -= greenOutSum
                blueSum -= blueOutSum

                stackStart = stackPointer - radius + diameter
                rgb = stack[stackStart % diameter]

                redOutSum -= rgb[0]
                greenOutSum -= rgb[1]
                blueOutSum -= rgb[2]

                if (x == 0) {
                    minValues[y] = (y + radiusPlusOne).coerceAtMost(heightMax) * width
                }
                val index = x + minValues[y]

                rgb[0] = reds[index]
                rgb[1] = greens[index]
                rgb[2] = blues[index]

                redInSum += rgb[0]
                greenInSum += rgb[1]
                blueInSum += rgb[2]

                redSum += redInSum
                greenSum += greenInSum
                blueSum += blueInSum

                stackPointer = (stackPointer + 1) % diameter
                rgb = stack[stackPointer]

                redOutSum += rgb[0]
                greenOutSum += rgb[1]
                blueOutSum += rgb[2]

                redInSum -= rgb[0]
                greenInSum -= rgb[1]
                blueInSum -= rgb[2]

                pixelIndex += width
            }
        }

        output.setPixels(pixels, 0, width, 0, 0, width, height)
        return output
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
