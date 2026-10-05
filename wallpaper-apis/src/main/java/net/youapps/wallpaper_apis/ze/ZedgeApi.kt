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
            query = """query browse_filteredList(${'$'}input: BrowseFilteredListFilterInput) {
  browse_filteredList(input: ${'$'}input) {
    ...browseFilteredListResource
  }
}

fragment browseFilteredListResource on BrowseContinuationItems {
  items {
    ...browseListItemResource

    ... on BrowseProfileItem {
      ...browseListProfileItemResource
    }
  }
  next
}

fragment browseListItemResource on BrowseItem {
  ... on BrowseWallpaperItem {
    id
    shareUrl
    licensed
    title
    description
    tags
    dateUploaded
    type
    paymentMethod {
      type
      price
    }
    meta {
      previewUrl
      microThumb
      thumbUrl
    }
    profile {
      id
      name
      avatarIconUrl
      verified
    }
  }

  ... on BrowseRingtoneItem {
    id
    licensed
    title
    type
    paymentMethod {
      type
      price
    }
    meta {
      audioUrl
      duration
      gradientStart
      gradientEnd
      thumbUrl
    }
    profile {
      id
      name
      avatarIconUrl
      verified
    }
  }

  ... on BrowseNotificationSoundItem {
    id
    licensed
    title
    type
    paymentMethod {
      type
      price
    }
    meta {
      audioUrl
      duration
      gradientStart
      gradientEnd
      thumbUrl
    }
    profile {
      id
      name
      avatarIconUrl
      verified
    }
  }

  ... on BrowseLiveWallpaperItem {
    id
    licensed
    title
    type
    paymentMethod {
      type
      price
    }
    meta {
      previewUrl
      thumbUrl
    }
    profile {
      id
      name
      avatarIconUrl
      verified
    }
  }
}

fragment browseListProfileItemResource on BrowseProfileItem {
  id
  type
  avatarUrl
  verified
  name
  shareUrl
}
""",
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