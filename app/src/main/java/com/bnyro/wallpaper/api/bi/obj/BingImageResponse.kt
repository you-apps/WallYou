package com.bnyro.wallpaper.api.bi.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class BingImageResponse(
    val images: List<BingImage> = emptyList()
)
