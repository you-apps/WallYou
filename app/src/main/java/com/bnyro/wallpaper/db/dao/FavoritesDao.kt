package com.bnyro.wallpaper.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bnyro.wallpaper.db.obj.Wallpaper

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites")
    fun getAll(): List<Wallpaper>

    @Query("SELECT * FROM favorites WHERE imgSrc LIKE :imgSrc")
    fun findBySrc(imgSrc: String): Wallpaper

    @Query("SELECT EXISTS (SELECT 1 FROM favorites WHERE imgSrc = :imgSrc)")
    fun exists(imgSrc: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg wallpaper: Wallpaper)

    @Delete
    fun delete(wallpaper: Wallpaper)

    @Query("DELETE FROM favorites")
    fun deleteAll()
}
