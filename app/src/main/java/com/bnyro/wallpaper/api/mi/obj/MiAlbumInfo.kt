package com.bnyro.wallpaper.api.mi.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MiAlbumInfo(
    val banner: String? = null,
    val id: String? = null,
    val idx: Int? = null,
    val total_count: Int? = null
)
