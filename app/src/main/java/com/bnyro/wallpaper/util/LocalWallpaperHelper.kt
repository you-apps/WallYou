package com.bnyro.wallpaper.util

import android.content.Context
import android.net.Uri
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object LocalWallpaperHelper {
    fun getWallpaperFiles(context: Context): List<File> {
        return getWallpaperDir(context).listFiles().orEmpty().toList()
    }

    fun deleteWallpapers(context: Context) {
        getWallpaperFiles(context).forEach {
            runCatching {
                it.delete()
            }
        }
    }

    fun saveWallpapers(context: Context, wallpapers: List<Uri>) {
        context.filesDir.listFiles().orEmpty().forEach { file ->
            file.delete()
        }
        wallpapers.forEachIndexed { index, uri ->
            CoroutineScope(Dispatchers.IO).launch {
                saveWallpaper(context, uri, index.toString())
            }
        }
    }

    private fun saveWallpaper(context: Context, uri: Uri, name: String) {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            File(getWallpaperDir(context), name).apply {
                createNewFile()
                outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }

    private fun getWallpaperDir(context: Context) = context.filesDir
}
