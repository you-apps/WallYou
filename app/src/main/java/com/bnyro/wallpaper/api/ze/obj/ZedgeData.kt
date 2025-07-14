package com.bnyro.wallpaper.api.ze.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ZedgeData(
    @SerialName("browse_filteredList") val browseFilteredlist: ZedgeBrowseFilteredList
)