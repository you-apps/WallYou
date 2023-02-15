package com.bnyro.wallpaper.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
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
            val request = buildRequest(context, imageURL)
                .target {
                    onSuccess(it.toBitmap())
                }
                .build()
            ImageLoader(context).enqueue(request)
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    suspend fun getSuspend(context: Context, imageURL: String?, forceReload: Boolean = false): Bitmap? {
        val request = buildRequest(context, imageURL).build()

        return ImageLoader(context).apply {
            if (forceReload) {
                diskCache?.clear()
                memoryCache?.clear()
            }
        }.execute(request).drawable?.toBitmap()
    }

    private fun buildRequest(context: Context, url: String?): ImageRequest.Builder {
        return ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false)
    }

    fun getLocalImage(context: Context, imagePath: Uri): Bitmap? {
        context.contentResolver.openInputStream(imagePath)?.use {
            return BitmapFactory.decodeStream(it)
        }
        return null
    }
}
