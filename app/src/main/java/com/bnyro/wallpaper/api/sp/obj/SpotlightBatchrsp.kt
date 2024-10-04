package com.bnyro.wallpaper.api.sp.obj

import kotlinx.serialization.Serializable

@Serializable
data class SpotlightBatchrsp(
    val items: List<SpotlightData>,
    val ver: String
)