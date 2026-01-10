package net.youapps.wallpaper_apis.wi.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WikiImageInfo(
    @SerialName("descriptionshorturl") val descriptionShortUrl: String,
    @SerialName("descriptionurl") val descriptionUrl: String,
    val url: String,
    val width: Int,
    val height: Int,
    @SerialName("thumburl") val thumbUrl: String? = null
)