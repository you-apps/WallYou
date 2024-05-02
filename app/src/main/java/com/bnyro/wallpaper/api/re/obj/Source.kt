package com.bnyro.wallpaper.api.re.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Source(
    @SerialName("url") val url: String? = null,
    @SerialName("width") val width: Int? = null,
    @SerialName("height") val height: Int? = null
) {
    val imgUrl
        get() = url?.replace("&amp;", "&")
}
