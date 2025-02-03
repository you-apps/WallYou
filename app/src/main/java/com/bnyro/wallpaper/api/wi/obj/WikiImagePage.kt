package com.bnyro.wallpaper.api.wi.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WikiImagePage(
    @SerialName("imageinfo") val imageInfo: List<WikiImageInfo>,
    @SerialName("imagerepository") val imageRepository: String,
    @SerialName("pageid") val pageId: Int,
    val title: String,
    val ns: Int
)