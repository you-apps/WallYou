package com.bnyro.wallpaper.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bnyro.wallpaper.db.obj.Wallpaper
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites")
    fun getAll(): List<Wallpaper>

    @Query("SELECT * FROM favorites WHERE favorite = 1")
    fun getFavorites(): List<Wallpaper>

    @Query("SELECT * FROM favorites WHERE favorite = 1 ORDER BY timeAdded DESC")
    fun getFavoritesFlow(): Flow<List<Wallpaper>>

    @Query("SELECT * FROM favorites WHERE inHistory = 1 ORDER BY timeAdded DESC")
    fun getHistoryFlow(): Flow<List<Wallpaper>>

    @Query("SELECT EXISTS (SELECT 1 FROM favorites WHERE favorite = 1 AND imgSrc = :imgSrc)")
    fun isLiked(imgSrc: String): Boolean

    @Query("SELECT * FROM favorites WHERE imgSrc = :imgSrc")
    fun findByImgSrc(imgSrc: String): Wallpaper?

    /**
     * Do not use this method directly unless when restoring backups
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(wallpapers: List<Wallpaper>)

    fun insert(wallpaper: Wallpaper, isFavorite: Boolean?, isHistory: Boolean?) {
        val existingWallpaper = findByImgSrc(wallpaper.imgSrc)

        wallpaper.favorite = isFavorite ?: existingWallpaper?.favorite ?: false
        wallpaper.inHistory = isHistory ?: existingWallpaper?.inHistory ?: false

        if (existingWallpaper != null) {
            updateWallpaper(wallpaper)
        } else {
            wallpaper.timeAdded = System.currentTimeMillis()
            insertAll(listOf(wallpaper))
        }
    }

    @Update
    fun updateWallpaper(wallpaper: Wallpaper)

    fun removeFromFavorites(wallpaper: Wallpaper) {
        insertAll(listOf(wallpaper.copy(favorite = false)))

        cleanup()
    }

    @Query("DELETE FROM favorites WHERE favorite = 0 and inHistory = 0")
    fun cleanup()
}
