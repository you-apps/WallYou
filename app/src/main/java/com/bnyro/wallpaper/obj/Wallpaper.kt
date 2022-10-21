package com.bnyro.wallpaper.obj

data class Wallpaper(
    val title: String?,
    val url: String?,
    var author: String?,
    val category: String?,
    val resolution: String?,
    val fileSize: Long?,
    val imgSrc: String?,
    var thumb: String?,
    val creationDate: String?
)
