package com.bnyro.wallpaper.api.re.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Images(
    @SerialName("source") val source: Source? = null,
    @SerialName("resolutions") val resolutions: List<Source> = emptyList()
)
