package com.bnyro.wallpaper.util

import android.content.Context
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.bnyro.wallpaper.obj.WallpaperConfig

object LocalWallpaperHelper {
    private fun getAllFilesInCurrentDir(directory: DocumentFile?): List<DocumentFile> {
        val files = mutableListOf<DocumentFile>()

        val contents = directory?.listFiles().orEmpty()
        files.addAll(contents.filter { it.isFile })

        contents.filter { it.isDirectory }.forEach {
            files.addAll(getAllFilesInCurrentDir(it))
        }

        return files
    }

    private val DocumentFile.isImage get() = type?.startsWith("image/") ?: false

    fun getLocalWalls(context: Context, config: WallpaperConfig): List<DocumentFile> {
        return config.localFolderUris.map {
            val dir = DocumentFile.fromTreeUri(context, it.toUri())
            getAllFilesInCurrentDir(dir)
        }
            .flatten()
            .filter { it.isImage }
    }
}
