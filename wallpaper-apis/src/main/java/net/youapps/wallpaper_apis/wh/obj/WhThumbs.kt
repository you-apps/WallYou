package net.youapps.wallpaper_apis.wh.obj

import kotlinx.serialization.Serializable

@Serializable
data class WhThumbs(
    val large: String? = null,
    val original: String? = null,
    val small: String? = null
)
