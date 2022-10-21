package com.bnyro.wallpaper.api.wh.obj

data class WhData(
    val category: String? = null,
    val colors: List<String>? = null,
    val created_at: String? = null,
    val dimension_x: Int? = null,
    val dimension_y: Int? = null,
    val favorites: Int? = null,
    val file_size: Long? = null,
    val file_type: String? = null,
    val id: String? = null,
    val path: String? = null,
    val purity: String? = null,
    val ratio: String? = null,
    val resolution: String? = null,
    val short_url: String? = null,
    val source: String? = null,
    val thumbs: WhThumbs? = null,
    val url: String? = null,
    val views: Int? = null
)
