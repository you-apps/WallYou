package com.bnyro.wallpaper.db.obj

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Wallpaper(
    @PrimaryKey val imgSrc: String,
    @ColumnInfo val title: String? = null,
    @ColumnInfo val url: String? = null,
    @ColumnInfo var author: String? = null,
    @ColumnInfo val category: String? = null,
    @ColumnInfo val resolution: String? = null,
    @ColumnInfo val fileSize: Long? = null,
    @ColumnInfo val thumb: String? = null,
    @ColumnInfo val creationDate: String? = null
)
