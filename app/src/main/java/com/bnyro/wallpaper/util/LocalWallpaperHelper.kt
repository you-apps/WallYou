package com.bnyro.wallpaper.util

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile

object LocalWallpaperHelper {
    fun getLocalWalls(context: Context): List<DocumentFile> {
        val directory = getDirectory() ?: return emptyList()
        return DocumentFile.fromTreeUri(context, directory)?.listFiles().orEmpty().toList()
    }

    fun getDirectory(): Uri? {
        val dirPref = Preferences.getString(Preferences.localWallpaperDirKey, "")
            .takeIf { it.orEmpty().isNotEmpty() }
        return dirPref?.toUri()
    }
}
