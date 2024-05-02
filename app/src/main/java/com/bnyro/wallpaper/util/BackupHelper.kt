package com.bnyro.wallpaper.util

import android.content.Context
import android.net.Uri
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.db.DatabaseHolder
import com.bnyro.wallpaper.db.obj.BackupFile
import com.bnyro.wallpaper.ext.toastFromMainThread
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream

object BackupHelper {
    const val JSON_MIME = "application/json"

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun backupFavorites(uri: Uri, context: Context) {
        try {
            val favorites = DatabaseHolder.Database.favoritesDao().getAll()
            val backupFile = BackupFile(favorites = favorites)
            context.contentResolver.openOutputStream(uri)?.use {
                RetrofitHelper.json.encodeToStream(backupFile, it)
            }
            context.toastFromMainThread(context.getString(R.string.success))
        } catch (e: Exception) {
            context.toastFromMainThread(e.localizedMessage)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun restoreFavorites(uri: Uri, context: Context) {
        try {
            context.contentResolver.openInputStream(uri)?.use {
                val backupFile = RetrofitHelper.json.decodeFromStream<BackupFile>(it)
                DatabaseHolder.Database.favoritesDao().insertAll(*backupFile.favorites.toTypedArray())
            }
            context.toastFromMainThread(context.getString(R.string.success))
        } catch (e: Exception) {
            context.toastFromMainThread(e.localizedMessage)
        }
    }
}