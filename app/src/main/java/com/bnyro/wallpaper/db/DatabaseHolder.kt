package com.bnyro.wallpaper.db

import android.content.Context
import androidx.room.Room

class DatabaseHolder {
    fun create(applicationContext: Context) {
        Database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    companion object {
        const val DATABASE_NAME = "WallYouDb"
        lateinit var Database: AppDatabase
    }
}
