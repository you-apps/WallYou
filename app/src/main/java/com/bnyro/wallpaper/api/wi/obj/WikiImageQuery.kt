package com.bnyro.wallpaper.api.wi.obj

import kotlinx.serialization.Serializable

@Serializable
data class WikiImageQuery(
    val pages: Map<String, WikiImagePage>
)