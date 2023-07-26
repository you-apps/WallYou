package com.bnyro.wallpaper.util

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.bnyro.wallpaper.enums.WallpaperConfig

object LocalWallpaperHelper {
    fun getLocalWalls(context: Context, config: WallpaperConfig): List<DocumentFile> {
        val directory = getDirectory(config) ?: return emptyList()
        return DocumentFile.fromTreeUri(context, directory)?.listFiles().orEmpty().toList()
    }

    fun getDirectory(config: WallpaperConfig) = config.localFolderUri?.toUri()
}
