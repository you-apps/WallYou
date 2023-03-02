package com.bnyro.wallpaper.util

import android.graphics.Bitmap
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
        val blurRadius = Preferences.getFloat(Preferences.blurKey, 1f).toInt()
        val grayScale = Preferences.getBoolean(Preferences.grayscaleKey, false)

        val blurredBitmap = bitmap.blur(blurRadius)
        return if (grayScale) blurredBitmap.grayScale() else blurredBitmap
    }
}
