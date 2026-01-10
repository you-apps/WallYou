package net.youapps.wallpaper_apis.us.obj

import kotlinx.serialization.Serializable

@Serializable
data class UsImage(
    val alt_description: String? = null,
    val blur_hash: String? = null,
    val color: String? = null,
    val created_at: String? = null,
    val height: Int? = null,
    val id: String? = null,
    val liked_by_user: Boolean = false,
    val likes: Int? = null,
    val links: UsLinks = UsLinks(),
    val premium: Boolean? = null,
    val promoted_at: String? = null,
    val updated_at: String? = null,
    val urls: UsUrls? = null,
    val user: UsUser? = null,
    val width: Int? = null,
    val views: Long? = null,
    val downloads: Long? = null
)
