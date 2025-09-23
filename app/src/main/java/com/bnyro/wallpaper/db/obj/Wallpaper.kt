package com.bnyro.wallpaper.db.obj

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "favorites")
@Serializable
data class Wallpaper(
    @PrimaryKey val imgSrc: String = "",
    @ColumnInfo val title: String? = null,
    @ColumnInfo val url: String? = null,
    @ColumnInfo var author: String? = null,
    @ColumnInfo val category: String? = null,
    @ColumnInfo val resolution: String? = null,
    @ColumnInfo val fileSize: Long? = null,
    @ColumnInfo val thumb: String? = null,
    @ColumnInfo val creationDate: String? = null,
    @ColumnInfo(defaultValue = "NULL") val description: String? = null,

    // The following ones may not be set by wallpaper APIs!
    @ColumnInfo(defaultValue = "1") var favorite: Boolean = false,
    @ColumnInfo(defaultValue = "0") var inHistory: Boolean = false,
    @ColumnInfo(defaultValue = "0") var timeAdded: Long = 0,
) {
    val preview get() = thumb ?: imgSrc
}
