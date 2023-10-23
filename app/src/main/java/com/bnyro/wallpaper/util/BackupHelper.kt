package com.bnyro.wallpaper.util

import android.content.Context
import android.net.Uri
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.db.DatabaseHolder
import com.bnyro.wallpaper.db.obj.BackupFile
import com.bnyro.wallpaper.ext.toastFromMainThread
import com.fasterxml.jackson.databind.ObjectMapper

object BackupHelper {
    const val JSON_MIME = "application/json"
    private val mapper by lazy {
        ObjectMapper()
    }

    suspend fun backupFavorites(uri: Uri, context: Context) {
        try {
            val favorites = DatabaseHolder.Database.favoritesDao().getAll()
            val backupFile = BackupFile(favorites = favorites)
            context.contentResolver.openOutputStream(uri)?.use {
                val favoritesJson = mapper.writeValueAsBytes(backupFile)
                it.write(favoritesJson)
            }
            context.toastFromMainThread(context.getString(R.string.success))
        } catch (e: Exception) {
            context.toastFromMainThread(e.localizedMessage)
        }
    }

    suspend fun restoreFavorites(uri: Uri, context: Context) {
        try {
            context.contentResolver.openInputStream(uri)?.use {
                val backupFile = mapper.readValue(it.readBytes(), BackupFile::class.java)
                DatabaseHolder.Database.favoritesDao().insertAll(*backupFile.favorites.toTypedArray())
            }
            context.toastFromMainThread(context.getString(R.string.success))
        } catch (e: Exception) {
            context.toastFromMainThread(e.localizedMessage)
        }
    }
}