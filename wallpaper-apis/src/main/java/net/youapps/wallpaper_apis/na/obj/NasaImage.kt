package net.youapps.wallpaper_apis.na.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NasaImage(
    @SerialName("hdurl") val hdUrl: String,
    val title: String,
    val alt: String,
    val copyright: String? = null,
    val date: String,
    @SerialName("media_type") val mediaType: String,
)