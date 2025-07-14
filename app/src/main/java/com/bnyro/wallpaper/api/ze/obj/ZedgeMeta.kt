package com.bnyro.wallpaper.api.ze.obj

import kotlinx.serialization.Serializable

@Serializable
data class ZedgeMeta(
    val microThumb: String,
    val previewUrl: String,
    val thumbUrl: String
)