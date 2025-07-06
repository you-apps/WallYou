package com.bnyro.wallpaper.api.re.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChildData(
    @SerialName("title") val title: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("preview") val preview: Preview? = null,
    @SerialName("thumbnail") val thumbnail: String? = null
)
