package net.youapps.wallpaper_apis.sp.obj

import kotlinx.serialization.Serializable

@Serializable
data class SpotlightInfo(
    val copyright: String,
    val ctaText: String,
    val ctaUri: String,
    val description: String,
    val dislikeGlyph: String,
    val entityId: String,
    val iconHoverText: String,
    val iconLabel: String,
    val landscapeImage: SpotlightImageItem,
    val likeGlyph: String,
    val portraitImage: SpotlightImageItem,
    val title: String
)