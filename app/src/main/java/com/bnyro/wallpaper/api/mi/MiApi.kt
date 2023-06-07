package com.bnyro.wallpaper.api.mi

import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitBuilder
import com.bnyro.wallpaper.util.TextUtils

class MiApi : Api() {
    override val name: String = "Mi Carousel"
    override val baseUrl: String = "https://wallpaper.pandora.xiaomi.com/api/a2/"
    override val filters: Map<String, List<String>> = mapOf(
        "categories" to listOf(
            "All",
            "Movie",
            "Animation",
            "Art",
            "Pets",
            "Food",
            "Car",
            "Sport",
            "Reading",
            "Life",
            "Home",
            "Illustration",
            "Travel",
            "Fashion",
            "Beauty",
            "Sexy",
            "Actress",
            "Actor",
            "News"
        )
    )
    override val supportsTags = false

    private val api = RetrofitBuilder.create(baseUrl, Carousel::class.java)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        return api.getWallpapers(
            channel_id = when (getQuery("categories")) {
                "All" -> null
                "Illustration" -> "CC01000017"
                "Animation" -> "CC01000017"
                "Art" -> "CC00000001"
                "Pets" -> "CC01000021"
                "Food" -> "CC00000009"
                "Car" -> "CC00000002"
                "Sport" -> "CC00000008"
                "Movie" -> "CC01000020"
                "Reading" -> "CC00000005"
                "Life" -> "f1920a8c-6007-11e5-950a-002590c3ab24"
                "Home" -> "CC01000014"
                "Travel" -> "6c25ff6d-39a4-11e5-83cb-70e52b253bb7"
                "Fashion" -> "CC00000012"
                "Beauty" -> "CC01000022"
                "Sexy" -> "CC01000013"
                "Actress" -> "78753f1a-39a4-11e5-83cb-70e52b253bb7"
                "Actor" -> "CC01000019"
                "News" -> "8532cf1f-39a4-11e5-83cb-70e52b253bb7"
                else -> "CC01000017"
            }
        ).items?.map {
            val image = it.images.firstOrNull()
            Wallpaper(
                imgSrc = image?.clUrl?.url_h.orEmpty(),
                title = it.meta?.title,
                url = it.images.firstOrNull()?.clUrl?.url_l,
                thumb = image?.clUrl?.url_h,
                creationDate = it.times?.create?.let { date -> TextUtils.formatDate(date) }
            )
        }.orEmpty()
    }

    override suspend fun getRandomWallpaperUrl(): String? {
        return getWallpapers(1).randomOrNull()?.imgSrc
    }
}
