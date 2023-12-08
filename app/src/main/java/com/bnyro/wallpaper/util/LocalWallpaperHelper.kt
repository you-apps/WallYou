package com.bnyro.wallpaper.util

import android.content.Context
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.bnyro.wallpaper.enums.WallpaperConfig

object LocalWallpaperHelper {
    fun getLocalWalls(context: Context, config: WallpaperConfig): List<DocumentFile> {
        return config.localFolderUris.map {
            DocumentFile.fromTreeUri(context, it.toUri())?.listFiles().orEmpty().toList()
        }.flatten()
    }
}
