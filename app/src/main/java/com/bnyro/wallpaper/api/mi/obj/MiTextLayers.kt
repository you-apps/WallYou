package com.bnyro.wallpaper.api.mi.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MiTextLayers(
    val c_pos_x: Int? = null,
    val c_pos_y: Int? = null,
    val color: String? = null,
    val txt: String? = null,
    val txt_size: Int? = null,
    val type: Int? = null
)
