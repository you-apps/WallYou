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
    suspend fun getAll(): List<Wallpaper>

    @Query("SELECT * FROM favorites WHERE favorite = 1 ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomFavorite(): Wallpaper?

    @Query("SELECT * FROM favorites WHERE favorite = 1 ORDER BY timeAdded DESC")
    fun getFavoritesFlow(): Flow<List<Wallpaper>>

    @Query("SELECT * FROM favorites WHERE inHistory = 1 ORDER BY timeAdded DESC")
    fun getHistoryFlow(): Flow<List<Wallpaper>>

    @Query("SELECT EXISTS (SELECT 1 FROM favorites WHERE favorite = 1 AND imgSrc = :imgSrc)")
    suspend fun isLiked(imgSrc: String): Boolean

    @Query("SELECT * FROM favorites WHERE imgSrc = :imgSrc")
    suspend fun findByImgSrc(imgSrc: String): Wallpaper?

    /**
     * Do not use this method directly unless when restoring backups
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(wallpapers: List<Wallpaper>)

    suspend fun insert(wallpaper: Wallpaper, isFavorite: Boolean?, isHistory: Boolean?) {
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
    suspend fun updateWallpaper(wallpaper: Wallpaper)

    suspend fun removeFromFavorites(wallpaper: Wallpaper) {
        insertAll(listOf(wallpaper.copy(favorite = false)))

        cleanup()
    }

    suspend fun removeFromHistory(wallpaper: Wallpaper) {
        insertAll(listOf(wallpaper.copy(inHistory = false)))

        cleanup()
    }

    @Query("DELETE FROM favorites WHERE favorite = 0 and inHistory = 0")
    suspend fun cleanup()
}
