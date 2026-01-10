package net.youapps.wallpaper_apis.le.obj

import kotlinx.serialization.Serializable

@Serializable
data class LemmyResponse(
    val posts: List<LemmyPostView> = emptyList()
)
