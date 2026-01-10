package net.youapps.wallpaper_apis.re.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Children(
    @SerialName("data") val childData: ChildData
)
