package com.bnyro.wallpaper.api.wh.obj

import kotlinx.serialization.Serializable

@Serializable
data class WhSearchResponse(
    val `data`: List<WhData>? = null,
    val meta: WhMeta? = null
)
