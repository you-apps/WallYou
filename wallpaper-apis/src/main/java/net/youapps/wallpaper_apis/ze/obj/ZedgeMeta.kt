package net.youapps.wallpaper_apis.ze.obj

import kotlinx.serialization.Serializable

@Serializable
data class ZedgeMeta(
    val microThumb: String,
    val previewUrl: String,
    val thumbUrl: String
)