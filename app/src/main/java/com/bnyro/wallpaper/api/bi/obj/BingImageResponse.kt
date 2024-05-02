package com.bnyro.wallpaper.api.bi.obj

import kotlinx.serialization.Serializable

@Serializable
data class BingImageResponse(
    val images: List<BingImage> = emptyList()
)
