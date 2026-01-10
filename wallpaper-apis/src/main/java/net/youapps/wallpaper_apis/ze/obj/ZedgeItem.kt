package net.youapps.wallpaper_apis.ze.obj

import kotlinx.serialization.Serializable

@Serializable
data class ZedgeItem(
    val id: String,
    val licensed: Boolean,
    val meta: ZedgeMeta,
    val profile: ZedgeProfile,
    val title: String,
    val description: String,
    val type: String,
    val tags: List<String>,
    val shareUrl: String,
    val dateUploaded: Long
)