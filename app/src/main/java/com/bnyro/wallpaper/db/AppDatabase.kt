package com.bnyro.wallpaper.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.bnyro.wallpaper.db.dao.FavoritesDao
import com.bnyro.wallpaper.db.obj.Wallpaper

@Database(
    version = 2,
    autoMigrations = [
        AutoMigration(1, 2)
    ],
    entities = [
        Wallpaper::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}
