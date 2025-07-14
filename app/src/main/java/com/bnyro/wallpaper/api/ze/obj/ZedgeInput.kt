package com.bnyro.wallpaper.api.ze.obj

import kotlinx.serialization.Serializable

@Serializable
data class ZedgeInput(
    val categories: List<String>,
    val sort: String,
    val next: String? = null,
    val itemType: String = "WALLPAPER",
    val profileType: String = "ANY",
    val size: Int = 24,
    val colors: List<Unit> = emptyList(),
    val keywords: List<Unit> = emptyList(),
    val maxDurationMs: Unit? = null,
    val maxPrice: Unit? = null,
    val minDurationMs: Unit? = null,
    val minPrice: Unit? = null,
)