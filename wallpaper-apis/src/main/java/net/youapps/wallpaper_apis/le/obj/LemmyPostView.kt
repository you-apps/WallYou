package net.youapps.wallpaper_apis.le.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LemmyPostView(
    @SerialName("creator") val creator: LemmyCreator = LemmyCreator(),
    @SerialName("post") val post: LemmyPost = LemmyPost(),
)
