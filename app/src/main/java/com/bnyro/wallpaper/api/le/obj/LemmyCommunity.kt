package com.bnyro.wallpaper.api.le.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LemmyCommunity(
    @JsonProperty("actor_id") val actorId: String = "",
    @JsonProperty("banner") val banner: String = "",
    @JsonProperty("deleted") val deleted: Boolean = false,
    @JsonProperty("description") val description: String = "",
    @JsonProperty("hidden") val hidden: Boolean = false,
    @JsonProperty("icon") val icon: String = "",
    @JsonProperty("id") val id: Int = 0,
    @JsonProperty("instance_id") val instanceId: Int = 0,
    @JsonProperty("local") val local: Boolean = false,
    @JsonProperty("name") val name: String = "",
    @JsonProperty("nsfw") val nsfw: Boolean = false,
    @JsonProperty("posting_restricted_to_mods") val postingRestrictedToMods: Boolean = false,
    @JsonProperty("published") val published: String = "",
    @JsonProperty("removed") val removed: Boolean = false,
    @JsonProperty("title") val title: String = "",
    @JsonProperty("updated") val updated: String = ""
)