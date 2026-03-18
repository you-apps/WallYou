package net.youapps.wallpaper_apis.wc.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WallpaperCaveMoreResponse(
    @SerialName("next_page") val nextPage: Int,
    val imgs: String,
)