package com.bnyro.wallpaper.ui.components

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ImageHelper {
    fun urlToBitmap(
        scope: CoroutineScope,
        imageURL: String,
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
}
