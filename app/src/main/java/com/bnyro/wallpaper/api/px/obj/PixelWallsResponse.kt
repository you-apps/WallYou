package com.bnyro.wallpaper.api.px.obj

import kotlinx.serialization.Serializable

@Serializable
data class PixelWallsResponse(
    val sha: String = "",
    val tree: List<PixelWall> = emptyList(),
    val truncated: Boolean = false,
    val url: String = ""
)