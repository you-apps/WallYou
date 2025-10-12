package com.bnyro.wallpaper.api.ze

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ScreenLockLandscape
import androidx.compose.ui.graphics.vector.ImageVector
import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.api.ze.obj.ZedgeInput
import com.bnyro.wallpaper.api.ze.obj.ZedgeRequest
import com.bnyro.wallpaper.api.ze.obj.ZedgeVariables
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.ext.amap
import com.bnyro.wallpaper.util.RetrofitHelper
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
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
        "colors" to listOf(
            "all",
            "black",
            "pink",
            "red",
            "blue",
            "white",
            "silver",
            "green",
            "gold"
        )
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

        return resp.items.amap {
            // fetching the full quality wallpaper source requires an additional request
            // per wallpaper, thus this can be quite slow
            val wallpaperSource = api.getWallpaperSource(it.id)
                .string()
                .let { rawText ->
                    CONTENT_URL_REGEX.find(rawText)?.groups?.get(1)?.value?.also { url ->
                        Log.d("zedge", "found maxres wallpaper url: $url")
                    }
                } ?: it.meta.previewUrl

            return@amap Wallpaper(
                url = it.shareUrl,
                imgSrc = wallpaperSource,
                thumb = it.meta.thumbUrl,
                title = it.title,
                description = it.description,
                creationDate = SimpleDateFormat.getDateInstance().format(it.dateUploaded),
                author = it.profile.name,
                category = it.tags.joinToString(", ")
            )
        }
            .awaitAll()
    }

    override suspend fun getRandomWallpaperUrl(): String? = getWallpapers(1).randomOrNull()?.imgSrc

    companion object {
        // example string: \"contentUrl\":\"https://is.zobj.net/image-server/v1/images?r=nip7NT8EvHH3sAqt78zrWnGJUpE0E3_Um9RWGrb6F6JQhuatkK8eoleHAUwUGbwIyMSDCzGdM3UdFH18qNHOdXfsFk-BSXBYMaBRyFJThDw86NiycO10zAvjQfQglcYUwf7mqMx0le77hwwmTX1TBznn3SX9j6yGQC_4uG0wCRKr0D5_I_POOE8yki9hUNwbOhd1ylsRId0KonLTHqDyK4vJLIxKAMLyrcvUUJaus6g7ma01aPq6cw7sReM\"}
        private val CONTENT_URL_REGEX = Regex("""contentUrl\d*\\":\\"(https://.*?)\\"""")
    }
}