package com.bnyro.wallpaper.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.FileOutputStream

object DownloadHelper {
    fun save(context: Context, uri: Uri?, bitmap: Bitmap?) {
        if (uri == null || bitmap == null) return

        context.contentResolver.openFileDescriptor(uri, "w")?.use {
            FileOutputStream(it.fileDescriptor).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 25, fos)
            }
        }
    }
}
