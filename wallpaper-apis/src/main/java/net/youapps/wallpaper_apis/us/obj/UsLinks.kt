package net.youapps.wallpaper_apis.us.obj

import kotlinx.serialization.Serializable

@Serializable
data class UsLinks(
    val download: String? = null,
    val download_location: String? = null,
    val html: String? = null,
    val self: String? = null
)
