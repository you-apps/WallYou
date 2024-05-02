package com.bnyro.wallpaper.api.re.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Preview(
    @SerialName("images") val images: ArrayList<Images>? = arrayListOf()
)
