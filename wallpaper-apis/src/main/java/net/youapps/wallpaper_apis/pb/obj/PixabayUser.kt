package net.youapps.wallpaper_apis.pb.obj

import kotlinx.serialization.Serializable

@Serializable
data class PixabayUser(
    val aboutMe: String,
    val avatarSrc: String,
    val firstName: String,
    val followerCount: Int,
    val id: Int,
    val isActive: Boolean,
    val isAvailableForHire: Boolean,
    val lastName: String,
    val profileUrl: String,
    val username: String
)