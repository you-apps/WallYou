package com.bnyro.wallpaper.util

import android.graphics.Bitmap
import com.google.android.renderscript.Toolkit

object BitmapProcessor {

    fun blur(image: Bitmap, radius: Float): Bitmap {
        return Toolkit.blur(image, radius.toInt())
    }
}
