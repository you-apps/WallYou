package com.bnyro.wallpaper.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bnyro.wallpaper.db.dao.FavoritesDao
import com.bnyro.wallpaper.db.obj.Wallpaper

@Database(
    version = 1,
    entities = [
        Wallpaper::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}
