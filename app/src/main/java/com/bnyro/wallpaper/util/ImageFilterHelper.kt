package com.bnyro.wallpaper.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ImageFilterHelper {
    fun getFilter(): Pair<ColorFilter, Dp> {
        val colorFilter = ColorFilter.colorMatrix(
            ColorMatrix().apply {
                setToSaturation(Preferences.getFloat(Preferences.saturationKey, 1f))
            }
        )
        val blur = Preferences.getFloat(Preferences.blurKey, 1f).dp
        return Pair(colorFilter, blur)
    }

    fun getBitmapFromColorMatrix(colorFilter: android.graphics.ColorFilter, sourceBitmap: Bitmap): Bitmap? {
        val ret = Bitmap.createBitmap(sourceBitmap.width, sourceBitmap.height, sourceBitmap.config)
        val canvas = Canvas(ret)
        val paint = Paint()
        paint.colorFilter = colorFilter
        canvas.drawBitmap(sourceBitmap, 0f, 0f, paint)
        return ret
    }
}
