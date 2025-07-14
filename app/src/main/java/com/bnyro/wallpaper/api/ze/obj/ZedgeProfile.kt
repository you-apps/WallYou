package com.bnyro.wallpaper.api.ze.obj

import kotlinx.serialization.Serializable

@Serializable
data class ZedgeProfile(
    val avatarIconUrl: String,
    val id: String,
    val name: String,
    val verified: Boolean
)