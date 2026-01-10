package net.youapps.wallpaper_apis.ze.obj

import kotlinx.serialization.Serializable

@Serializable
data class ZedgeBrowseFilteredList(
    val items: List<ZedgeItem>,
    val next: String
)