package net.youapps.wallpaper_apis.bi.obj

import kotlinx.serialization.Serializable

@Serializable
data class BingImageResponse(
    val images: List<BingImage> = emptyList()
)
