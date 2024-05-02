package com.bnyro.wallpaper.api.px.obj

import kotlinx.serialization.Serializable

@Serializable
data class PixelWall(
    val mode: String = "",
    val path: String = "",
    val sha: String = "",
    val size: Long = 0,
    val type: String = "",
    val url: String = ""
)