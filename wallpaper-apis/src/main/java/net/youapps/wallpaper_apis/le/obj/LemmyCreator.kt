package net.youapps.wallpaper_apis.le.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LemmyCreator(
    @SerialName("name") val name: String = "",
)
