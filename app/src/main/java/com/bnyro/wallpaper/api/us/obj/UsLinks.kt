package com.bnyro.wallpaper.api.us.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UsLinks(
    val download: String? = null,
    val download_location: String? = null,
    val html: String? = null,
    val self: String? = null
)
