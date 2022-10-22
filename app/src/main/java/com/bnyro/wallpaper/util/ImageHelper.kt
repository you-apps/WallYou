package com.bnyro.wallpaper.util

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.executeBlocking
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ImageHelper {
    fun urlToBitmap(
        scope: CoroutineScope,
        imageURL: String?,
        context: Context,
        onSuccess: (bitmap: Bitmap) -> Unit
    ) {
        scope.launch(Dispatchers.IO) {
            val request = ImageRequest.Builder(context)
                .data(imageURL)
                .allowHardware(false)
                .target {
                    onSuccess(it.toBitmap())
                }
                .build()
            ImageLoader(context).enqueue(request)
        }
    }

    fun getBlocking(context: Context, imageURL: String?): Bitmap? {
        val request: ImageRequest = ImageRequest.Builder(context)
            .data(imageURL)
            .build()

        return ImageLoader(context).executeBlocking(request).drawable?.toBitmap()
    }
}
