package net.youapps.wallpaper_apis.re.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RedditListingResponse(
    val kind: String? = null,
    val data: RedditListingData? = null
)

@Serializable
data class RedditListingData(
    val after: String? = null,
    val before: String? = null,
    val dist: Int? = null,
    val children: List<RedditChild> = emptyList()
)

@Serializable
data class RedditChild(
    val kind: String? = null,
    val data: RedditPostData? = null
)

@Serializable
data class RedditPostData(
    val id: String = "",
    val name: String = "",
    val title: String = "",
    val author: String? = null,
    val subreddit: String? = null,
    val permalink: String? = null,
    val url: String = "",
    val thumbnail: String? = null,
    @SerialName("over_18")
    val over18: Boolean = false,
    val score: Int = 0,
    @SerialName("created_utc")
    val createdUtc: Double = 0.0,
    @SerialName("is_gallery")
    val isGallery: Boolean = false,
    @SerialName("post_hint")
    val postHint: String? = null,
    val preview: RedditPreview? = null,
    @SerialName("gallery_data")
    val galleryData: RedditGalleryData? = null,
    @SerialName("media_metadata")
    val mediaMetadata: Map<String, RedditMediaItem>? = null
)

@Serializable
data class RedditPreview(
    val images: List<RedditPreviewImage> = emptyList(),
    val enabled: Boolean = false
)

@Serializable
data class RedditPreviewImage(
    val source: RedditImageSource? = null,
    val resolutions: List<RedditImageSource> = emptyList(),
    val id: String? = null
)

@Serializable
data class RedditImageSource(
    val url: String = "",
    val width: Int = 0,
    val height: Int = 0
)

@Serializable
data class RedditGalleryData(
    val items: List<RedditGalleryItem> = emptyList()
)

@Serializable
data class RedditGalleryItem(
    @SerialName("media_id")
    val mediaId: String = "",
    val id: Long? = null,
    val caption: String? = null
)

@Serializable
data class RedditMediaItem(
    val status: String? = null,
    val e: String? = null,
    val m: String? = null,
    val s: RedditMediaSource? = null,
    val p: List<RedditMediaSource> = emptyList()
)

@Serializable
data class RedditMediaSource(
    val x: Int = 0,
    val y: Int = 0,
    val u: String? = null,
    val gif: String? = null
)
