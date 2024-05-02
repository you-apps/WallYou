package com.bnyro.wallpaper.api.wh.obj

import kotlinx.serialization.Serializable

@Serializable
data class WhMeta(
    val current_page: Int? = null,
    val last_page: Int? = null,
    val per_page: Int? = null,
    val total: Int? = null
)
