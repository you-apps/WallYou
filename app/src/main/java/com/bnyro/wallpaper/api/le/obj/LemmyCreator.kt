package com.bnyro.wallpaper.api.le.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LemmyCreator(
    @JsonProperty("actor_id") val actorId: String = "",
    @JsonProperty("admin") val admin: Boolean = false,
    @JsonProperty("avatar") val avatar: String = "",
    @JsonProperty("banned") val banned: Boolean = false,
    @JsonProperty("banner") val banner: String = "",
    @JsonProperty("bot_account") val botAccount: Boolean = false,
    @JsonProperty("deleted") val deleted: Boolean = false,
    @JsonProperty("id") val id: Int = 0,
    @JsonProperty("instance_id") val instanceId: Int = 0,
    @JsonProperty("local") val local: Boolean = false,
    @JsonProperty("name") val name: String = "",
    @JsonProperty("published") val published: String = ""
)