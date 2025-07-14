package com.bnyro.wallpaper.api.ze.obj

import kotlinx.serialization.Serializable

@Serializable
data class ZedgeRequest(
    val operationName: String = "browse_filteredList",
    val query: String,
    val variables: ZedgeVariables
)