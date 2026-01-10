package net.youapps.wallpaper_apis.ze.obj

import kotlinx.serialization.Serializable

@Serializable
data class ZedgeRequest(
    val operationName: String = "browse_filteredList",
    val query: String,
    val variables: ZedgeVariables
)