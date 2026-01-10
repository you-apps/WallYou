package net.youapps.wallpaper_apis.us.obj

import kotlinx.serialization.Serializable

@Serializable
data class UsUser(
    val accepted_tos: Boolean? = null,
    val bio: String? = null,
    val first_name: String? = null,
    val for_hire: Boolean? = null,
    val id: String? = null,
    val instagram_username: String? = null,
    val last_name: String? = null,
    val location: String? = null,
    val name: String? = null,
    val portfolio_url: String? = null,
    val total_collections: Int? = null,
    val total_likes: Int? = null,
    val total_photos: Int? = null,
    val twitter_username: String? = null,
    val updated_at: String? = null,
    val username: String? = null
)
