package com.bnyro.wallpaper.api.wi.obj

import kotlinx.serialization.Serializable

@Serializable
data class WikiImagesResponse(
    val query: WikiImageQuery
)