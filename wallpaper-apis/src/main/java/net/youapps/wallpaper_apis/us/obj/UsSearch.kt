package net.youapps.wallpaper_apis.us.obj

import kotlinx.serialization.Serializable

@Serializable
data class UsSearch(
    val total: Long? = null,
    val total_pages: Long? = null,
    val results: List<UsImage> = listOf()
)
