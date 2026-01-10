package net.youapps.wallpaper_apis.ze.obj

import kotlinx.serialization.Serializable

@Serializable
data class ZedgeInput(
    val categories: List<String>,
    val sort: String,
    val colors: List<String>,
    val next: String? = null,
    val itemType: String = "WALLPAPER",
    val profileType: String = "ANY",
    val size: Int = 10,
    val keywords: List<String> = emptyList(),
    val maxDurationMs: Int? = null,
    val minDurationMs: Int? = null,
    // enforce free wallpapers only
    val maxPrice: Int = 0,
    val minPrice: Int = 0,
)