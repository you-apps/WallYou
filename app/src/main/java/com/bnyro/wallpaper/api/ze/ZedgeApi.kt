package com.bnyro.wallpaper.api.ze

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ScreenLockLandscape
import androidx.compose.ui.graphics.vector.ImageVector
import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.api.ze.obj.ZedgeInput
import com.bnyro.wallpaper.api.ze.obj.ZedgeRequest
import com.bnyro.wallpaper.api.ze.obj.ZedgeVariables
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitHelper
import java.text.SimpleDateFormat

class ZedgeApi : Api() {
    override val name: String = "Zedge"
    override val icon: ImageVector = Icons.Default.ScreenLockLandscape
    override val baseUrl: String = "https://www.zedge.net"
    override val filters: Map<String, List<String>> = mapOf(
        "category" to listOf(
            "All",
            "Funny",
            "Technology",
            "Entertainment",
            "Music",
            "Nature",
            "Drawings",
            "Sports",
            "Brands",
            "Cars & Vehicles",
            "Other",
            "Animals",
            "Patterns",
            "Bollywood",
            "Anime",
            "Games",
            "Holidays",
            "Designs",
            "Love",
            "News & Politics",
            "People",
            "Sayings",
            "Spiritual",
            "Space",
            "Comics"
        ),
        "sort" to listOf("Relevant", "Popular", "Newest"),
        "colors" to listOf("all", "black", "pink", "red", "blue", "white", "silver", "green", "gold")
    )

    private val api = RetrofitHelper.create<Zedge>(baseUrl)
    private var nextPage: String? = null

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        if (page == 1) nextPage = null

        val category = getQuery("category").takeIf { it != "All" }?.let { category ->
            listOf(category.uppercase().replace(" & ", "_N_"))
        } ?: emptyList()

        val reqBody = ZedgeRequest(
            query = "\n    query browse_filteredList(\$input: BrowseFilteredListFilterInput) {\n      browse_filteredList(input: \$input) {\n        ...browseFilteredListResource\n      }\n    }\n    \n  fragment browseFilteredListResource on BrowseContinuationItems {\n    items {\n      ...browseListItemResource\n\n      ... on BrowseProfileItem {\n        ...browseListProfileItemResource\n      }\n    }\n    next\n  }\n  \n  fragment browseListItemResource on BrowseItem {\n    ... on BrowseWallpaperItem {\n      id\n      shareUrl\n      licensed\n      title\n      description\n      tags\n      dateUploaded\n      type\n      paymentMethod {\n        type\n        price\n      }\n      meta {\n        previewUrl\n        microThumb\n        thumbUrl\n      }\n      profile {\n        id\n        name\n        avatarIconUrl\n        verified\n      }\n    }\n\n    ... on BrowseRingtoneItem {\n      id\n      licensed\n      title\n      type\n      paymentMethod {\n        type\n        price\n      }\n      meta {\n        audioUrl\n        duration\n        gradientStart\n        gradientEnd\n        thumbUrl\n      }\n      profile {\n        id\n        name\n        avatarIconUrl\n        verified\n      }\n    }\n\n    ... on BrowseNotificationSoundItem {\n      id\n      licensed\n      title\n      type\n      paymentMethod {\n        type\n        price\n      }\n      meta {\n        audioUrl\n        duration\n        gradientStart\n        gradientEnd\n        thumbUrl\n      }\n      profile {\n        id\n        name\n        avatarIconUrl\n        verified\n      }\n    }\n\n    ... on BrowseLiveWallpaperItem {\n      id\n      licensed\n      title\n      type\n      paymentMethod {\n        type\n        price\n      }\n      meta {\n        previewUrl\n        thumbUrl\n      }\n      profile {\n        id\n        name\n        avatarIconUrl\n        verified\n      }\n    }\n  }\n\n  \n  fragment browseListProfileItemResource on BrowseProfileItem {\n    id\n    type\n    avatarUrl\n    verified\n    name\n    shareUrl\n  }\n\n\n  ",
            variables = ZedgeVariables(
                ZedgeInput(
                    categories = category,
                    sort = getQuery("sort").uppercase(),
                    colors = getQuery("colors")
                        .takeIf { it != "all" }
                        ?.let { listOf(it) }
                        .orEmpty(),
                    next = nextPage
                )
            )
        )
        val resp = api.getWallpapers(reqBody).data.browseFilteredlist
        nextPage = resp.next

        return resp.items.map {
            Wallpaper(
                url = it.shareUrl,
                imgSrc = it.meta.previewUrl,
                thumb = it.meta.thumbUrl,
                title = it.title,
                description = it.description,
                creationDate = SimpleDateFormat.getDateInstance().format(it.dateUploaded),
                author = it.profile.name,
                category = it.tags.joinToString(", ")
            )
        }
    }

    override suspend fun getRandomWallpaperUrl(): String? = getWallpapers(1).randomOrNull()?.imgSrc
}