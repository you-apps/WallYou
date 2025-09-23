package com.bnyro.wallpaper.api.pb.obj

import kotlinx.serialization.Serializable

@Serializable
data class PixabayResponse(
    val page: PixabayPage,
)