package com.bnyro.wallpaper.api.us.obj

import kotlinx.serialization.Serializable

@Serializable
data class UsUrls(
    val full: String? = null,
    val raw: String? = null,
    val regular: String? = null,
    val small: String? = null,
    val small_s3: String? = null,
    val thumb: String? = null
)
