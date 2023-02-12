package com.bnyro.wallpaper.api.us.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UsImage(
    val alt_description: String? = null,
    val blur_hash: String? = null,
    val color: String? = null,
    val created_at: String? = null,
    val current_user_collections: List<Any> = listOf(),
    val description: Any? = null,
    val height: Int? = null,
    val id: String? = null,
    val liked_by_user: Boolean = false,
    val likes: Int? = null,
    val links: UsLinks = UsLinks(),
    val premium: Boolean? = null,
    val promoted_at: String? = null,
    val sponsorship: Any? = null,
    val topic_submissions: Any? = null,
    val updated_at: String? = null,
    val urls: UsUrls? = null,
    val user: UsUser? = null,
    val width: Int? = null,
    val views: Long? = null,
    val downloads: Long? = null
)
