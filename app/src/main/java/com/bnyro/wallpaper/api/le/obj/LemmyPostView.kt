package com.bnyro.wallpaper.api.le.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LemmyPostView(
    @JsonProperty("community") val community: LemmyCommunity = LemmyCommunity(),
    @JsonProperty("creator") val creator: LemmyCreator = LemmyCreator(),
    @JsonProperty("creator_banned_from_community") val creatorBannedFromCommunity: Boolean = false,
    @JsonProperty("creator_blocked") val creatorBlocked: Boolean = false,
    @JsonProperty("post") val post: LemmyPost = LemmyPost(),
    @JsonProperty("read") val read: Boolean = false,
    @JsonProperty("saved") val saved: Boolean = false,
    @JsonProperty("subscribed") val subscribed: String = "",
    @JsonProperty("unread_comments") val unreadComments: Int = 0
)
