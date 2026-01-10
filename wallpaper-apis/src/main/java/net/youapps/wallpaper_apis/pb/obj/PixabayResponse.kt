package net.youapps.wallpaper_apis.pb.obj

import kotlinx.serialization.Serializable

@Serializable
data class PixabayResponse(
    val page: PixabayPage,
)