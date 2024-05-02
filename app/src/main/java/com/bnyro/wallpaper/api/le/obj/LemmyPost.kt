package com.bnyro.wallpaper.api.le.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LemmyPost(
    @SerialName("ap_id") val postUrl: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("published") val published: String = "",
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,
)
