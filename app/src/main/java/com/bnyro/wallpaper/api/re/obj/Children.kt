package com.bnyro.wallpaper.api.re.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Children(
    @SerialName("data") val childData: ChildData
)
