package com.bnyro.wallpaper.api.px.obj

data class PixelWallsResponse(
    val sha: String = "",
    val tree: List<PixelWall> = emptyList(),
    val truncated: Boolean = false,
    val url: String = ""
)