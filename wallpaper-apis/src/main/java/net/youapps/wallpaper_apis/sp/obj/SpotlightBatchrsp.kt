package net.youapps.wallpaper_apis.sp.obj

import kotlinx.serialization.Serializable

@Serializable
data class SpotlightBatchrsp(
    val items: List<SpotlightData>,
    val ver: String
)