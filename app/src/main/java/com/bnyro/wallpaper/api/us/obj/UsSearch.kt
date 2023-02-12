package com.bnyro.wallpaper.api.us.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UsSearch(
    val total: Long? = null,
    val total_pages: Long? = null,
    val results: List<UsImage> = listOf()
)
