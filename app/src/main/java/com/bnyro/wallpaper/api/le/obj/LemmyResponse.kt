package com.bnyro.wallpaper.api.le.obj

import kotlinx.serialization.Serializable

@Serializable
data class LemmyResponse(
    val posts: List<LemmyPostView> = emptyList()
)
