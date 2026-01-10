package net.youapps.wallpaper_apis.pb.obj

import kotlinx.serialization.Serializable

@Serializable
data class PixabayResult(
    val alt: String,
    val attributionHtml: String,
    val canvaRetouchUrl: String,
    val commentCount: Int,
    val commentHref: String,
    val description: String,
    val downloadCount: Int,
    val height: Int,
    val href: String,
    val id: Int,
    val isAiGenerated: Boolean,
    val isEditorsChoice: Boolean,
    val isLowQuality: Boolean,
    val likeCount: Int,
    val likeHref: String,
    val mediaDescriptiveType: String,
    val mediaSubType: Int,
    val mediaType: String,
    val name: String,
    val nsfw: Boolean,
    val qualityStatus: Int,
    val sources: Map<String, String>,
    val tagLinks: String,
    val tagList: List<List<String>>,
    val title: String,
    val uploadDate: String,
    val user: PixabayUser,
    val vector: Boolean,
    val viewCount: Int,
    val width: Int
)