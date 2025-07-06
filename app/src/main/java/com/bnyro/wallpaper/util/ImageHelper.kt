package com.bnyro.wallpaper.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import androidx.exifinterface.media.ExifInterface
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
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
            context.imageLoader.enqueue(request)
        }
    }

    suspend fun urlToBitmap(
        imageURL: String?,
        context: Context,
        forceReload: Boolean = false
    ): Bitmap? {
        val imageRequestBuilder = ImageRequest.Builder(context)
            .data(imageURL)
            .allowHardware(false)

        if (forceReload) {
            // disable all caches for this request to fresh-reload the image
            imageRequestBuilder
                .diskCachePolicy(CachePolicy.DISABLED)
                .memoryCachePolicy(CachePolicy.DISABLED)
                .networkCachePolicy(CachePolicy.DISABLED)
        }

        val result = context.imageLoader.execute(imageRequestBuilder.build())

        if (result is SuccessResult) {
            return result.drawable.toBitmap().copy(Bitmap.Config.ARGB_8888, false)
        }
        return null
    }

    private fun rotateBitmap(bitmap: Bitmap, exifInterface: ExifInterface): Bitmap {
        val orientation = runCatching {
            exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        }.getOrElse { return bitmap }

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90, ExifInterface.ORIENTATION_TRANSPOSE -> rotateImage(
                bitmap,
                90f
            )

            ExifInterface.ORIENTATION_ROTATE_180, ExifInterface.ORIENTATION_FLIP_VERTICAL -> rotateImage(
                bitmap,
                180f
            )

            ExifInterface.ORIENTATION_ROTATE_270, ExifInterface.ORIENTATION_TRANSVERSE -> rotateImage(
                bitmap,
                -90f
            )

            else -> bitmap
        }
    }

    private fun rotateImage(bitmap: Bitmap, rotation: Float): Bitmap {
        val matrix = Matrix().apply {
            setScale(-1f, 1f)
            setRotate(rotation)
        }
        val oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return oriented
    }

    fun getLocalImage(context: Context, imagePath: Uri): Bitmap? {
        return context.contentResolver.openFileDescriptor(imagePath, "r")?.use {
            val bitmap = BitmapFactory.decodeFileDescriptor(it.fileDescriptor) ?: return null
            val exifInterface =
                ExifInterface(it.fileDescriptor) // This will change fileDescriptor position
            rotateBitmap(bitmap, exifInterface)
        }
    }
}
