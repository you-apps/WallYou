package net.youapps.wallpaper_apis.wi.obj

import kotlinx.serialization.Serializable

@Serializable
data class WikiImageQuery(
    val pages: Map<String, WikiImagePage>
)