package net.youapps.wallpaper_apis.ze

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper
import net.youapps.wallpaper_apis.WallpaperApi
import net.youapps.wallpaper_apis.ze.obj.ZedgeInput
import net.youapps.wallpaper_apis.ze.obj.ZedgeItem
import net.youapps.wallpaper_apis.ze.obj.ZedgeRequest
import net.youapps.wallpaper_apis.ze.obj.ZedgeVariables
import java.text.SimpleDateFormat

private suspend fun <A, B> List<A>.amap(f: suspend (A) -> B): List<Deferred<B>> =
    map { coroutineScope { async { f(it) } } }

class ZedgeApi : WallpaperApi() {
    override val name: String = "Zedge"
    override val baseUrl: String = "https://www.zedge.net"
    override val availableFilters: Map<String, List<String>> = mapOf(
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

        return requestWallpapers().amap {
            val wallpaperSource = getFullQualityWallpaperUrl(it.id) ?: it.meta.previewUrl

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
        }.awaitAll()
    }

    private suspend fun requestWallpapers(): List<ZedgeItem> {
        val category = selectedFilters["category"].takeIf { it != "All" }?.let { category ->
            listOf(category.uppercase().replace(" & ", "_N_"))
        } ?: emptyList()

        val reqBody = ZedgeRequest(
            query = "\n    query browse_filteredList(\$input: BrowseFilteredListFilterInput) {\n      browse_filteredList(input: \$input) {\n        ...browseFilteredListResource\n      }\n    }\n    \n  fragment browseFilteredListResource on BrowseContinuationItems {\n    items {\n      ...browseListItemResource\n\n      ... on BrowseProfileItem {\n        ...browseListProfileItemResource\n      }\n    }\n    next\n  }\n  \n  fragment browseListItemResource on BrowseItem {\n    ... on BrowseWallpaperItem {\n      id\n      shareUrl\n      licensed\n      title\n      description\n      tags\n      dateUploaded\n      type\n      paymentMethod {\n        type\n        price\n      }\n      meta {\n        previewUrl\n        microThumb\n        thumbUrl\n      }\n      profile {\n        id\n        name\n        avatarIconUrl\n        verified\n      }\n    }\n\n    ... on BrowseRingtoneItem {\n      id\n      licensed\n      title\n      type\n      paymentMethod {\n        type\n        price\n      }\n      meta {\n        audioUrl\n        duration\n        gradientStart\n        gradientEnd\n        thumbUrl\n      }\n      profile {\n        id\n        name\n        avatarIconUrl\n        verified\n      }\n    }\n\n    ... on BrowseNotificationSoundItem {\n      id\n      licensed\n      title\n      type\n      paymentMethod {\n        type\n        price\n      }\n      meta {\n        audioUrl\n        duration\n        gradientStart\n        gradientEnd\n        thumbUrl\n      }\n      profile {\n        id\n        name\n        avatarIconUrl\n        verified\n      }\n    }\n\n    ... on BrowseLiveWallpaperItem {\n      id\n      licensed\n      title\n      type\n      paymentMethod {\n        type\n        price\n      }\n      meta {\n        previewUrl\n        thumbUrl\n      }\n      profile {\n        id\n        name\n        avatarIconUrl\n        verified\n      }\n    }\n  }\n\n  \n  fragment browseListProfileItemResource on BrowseProfileItem {\n    id\n    type\n    avatarUrl\n    verified\n    name\n    shareUrl\n  }\n\n\n  ",
            variables = ZedgeVariables(
                ZedgeInput(
                    categories = category,
                    sort = selectedFilters["sort"]!!.uppercase(),
                    colors = selectedFilters["colors"]
                        .takeIf { it != "all" }
                        ?.let { listOf(it) }
                        .orEmpty(),
                    next = nextPage
                )
            )
        )

        return api.getWallpapers(reqBody).data.browseFilteredlist.also {
            nextPage = it.next
        }.items
    }

    private suspend fun getFullQualityWallpaperUrl(id: String): String? {
        // fetching the full quality wallpaper source requires an additional request
        // per wallpaper, thus this can be quite slow
        return api.getWallpaperSource(id)
            .string()
            .let { rawText ->
                CONTENT_URL_REGEX.find(rawText)?.groups?.get(1)?.value?.also { url ->
                    println("zedge: found maxres wallpaper url: $url")
                }
            }
    }

    override suspend fun getRandomWallpaperUrl(): String? =
        requestWallpapers().firstOrNull()?.let {
            getFullQualityWallpaperUrl(it.id) ?: it.meta.previewUrl
        }

    companion object {
        // example string: \"contentUrl1\":\"https://is.zobj.net/image-server/v1/images?r=nip7NT8EvHH3sAqt78zrWnGJUpE0E3_Um9RWGrb6F6JQhuatkK8eoleHAUwUGbwIyMSDCzGdM3UdFH18qNHOdXfsFk-BSXBYMaBRyFJThDw86NiycO10zAvjQfQglcYUwf7mqMx0le77hwwmTX1TBznn3SX9j6yGQC_4uG0wCRKr0D5_I_POOE8yki9hUNwbOhd1ylsRId0KonLTHqDyK4vJLIxKAMLyrcvUUJaus6g7ma01aPq6cw7sReM\"}
        private val CONTENT_URL_REGEX = Regex("""contentUrl\d*\\":\\"(https://.*?)\\"""")
    }
}