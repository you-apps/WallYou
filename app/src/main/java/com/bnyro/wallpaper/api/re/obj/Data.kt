package com.bnyro.wallpaper.api.re.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("after") val after: String? = null,
    @SerialName("children") val children: ArrayList<Children>? = arrayListOf()
)
