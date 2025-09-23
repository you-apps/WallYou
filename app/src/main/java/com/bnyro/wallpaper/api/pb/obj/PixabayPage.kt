package com.bnyro.wallpaper.api.pb.obj

import kotlinx.serialization.Serializable

@Serializable
data class PixabayPage(
    val mediaType: String,
    val minHeight: Int,
    val minWidth: Int,
    val order: String,
    val orientation: String,
    val page: Int,
    val pageType: String,
    val pages: Int,
    val perPage: Int,
    val query: String,
    val results: List<PixabayResult>,
    val retouchInCanvaMobileVariant: Int,
    val safesearch: String,
    val searchType: String,
    val showAds: String,
    val sponsorLinkHref: String,
    val sponsorViewMoreHref: String,
    val suggest: String,
    val total: Int,
)