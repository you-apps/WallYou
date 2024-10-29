package com.bnyro.wallpaper.api.na.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NasaImage(
    val url: String? = null,
    val title: String,
    val explanation: String,
    val copyright: String? = null,
    val date: String,
    @SerialName("hdurl") val hdUrl: String? = null,
    @SerialName("media_type") val mediaType: String,
)