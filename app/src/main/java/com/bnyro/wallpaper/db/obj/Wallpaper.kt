package com.bnyro.wallpaper.db.obj

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Wallpaper(
    @PrimaryKey val imgSrc: String,
    @ColumnInfo val title: String?,
    @ColumnInfo val url: String?,
    @ColumnInfo var author: String?,
    @ColumnInfo val category: String?,
    @ColumnInfo val resolution: String?,
    @ColumnInfo val fileSize: Long?,
    @ColumnInfo val thumb: String?,
    @ColumnInfo val creationDate: String?
)
