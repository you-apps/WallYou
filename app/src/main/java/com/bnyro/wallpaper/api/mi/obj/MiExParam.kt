package com.bnyro.wallpaper.api.mi.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MiExParam(
    val img_level: Int? = null
)
