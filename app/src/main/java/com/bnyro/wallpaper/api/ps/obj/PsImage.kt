package com.bnyro.wallpaper.api.ps.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PsImage(
    val author: String? = null,
    @SerialName("download_url") val downloadUrl: String? = null,
    val height: Int? = null,
    val id: String? = null,
    val url: String? = null,
    val width: Int? = null
)
