package com.bnyro.wallpaper.api.le.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LemmyResponse(
    val posts: List<LemmyPostView> = emptyList()
)
