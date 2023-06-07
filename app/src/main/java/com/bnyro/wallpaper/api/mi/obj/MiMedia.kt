package com.bnyro.wallpaper.api.mi.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MiMedia(
    val icon: String? = null,
    val id: String? = null,
    val name: String? = null
)
