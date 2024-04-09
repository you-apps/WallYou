package com.bnyro.wallpaper.api.le.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LemmyPostView(
    @JsonProperty("creator") val creator: LemmyCreator = LemmyCreator(),
    @JsonProperty("post") val post: LemmyPost = LemmyPost(),
)
