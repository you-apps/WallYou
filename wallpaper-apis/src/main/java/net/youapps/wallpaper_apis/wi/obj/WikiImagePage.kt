package net.youapps.wallpaper_apis.wi.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WikiImagePage(
    @SerialName("imageinfo") val imageInfo: List<WikiImageInfo>,
    @SerialName("imagerepository") val imageRepository: String,
    val title: String,
    val ns: Int
)