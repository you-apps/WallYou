package com.bnyro.wallpaper.api.ze.obj

import kotlinx.serialization.Serializable

@Serializable
data class ZedgeItem(
    val id: String,
    val licensed: Boolean,
    val meta: ZedgeMeta,
    val profile: ZedgeProfile,
    val title: String,
    val type: String
)