package com.bnyro.wallpaper.api.sp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.api.sp.obj.SpotlightImage
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitHelper
import kotlinx.serialization.decodeFromString
import java.util.Locale

// Credits to https://github.com/ORelio/Spotlight-Downloader/blob/master/SpotlightAPI.md

class SpApi: Api() {
    override val name = "Spotlight"
    override val baseUrl = "https://fd.api.iris.microsoft.com"
    override val icon = Icons.Default.LightMode

    override val filters: Map<String, List<String>> = mapOf(
        "country" to listOf("US") + Locale.getISOCountries().toList().filter { it != "US" }.sorted()
    )

    private val api = RetrofitHelper.create(baseUrl, Spotlight::class.java)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        val country = getQuery("country")
        val apiResp = api.getLatestPage(country = country, locale = "en-${country}")
        val images = apiResp.batchrsp.items.map {
            RetrofitHelper.json.decodeFromString<SpotlightImage>(it.item)
        }
        return images.map {
            Wallpaper(
                title = it.ad.title,
                imgSrc = it.ad.portraitImage.asset,
                author = it.ad.copyright,
                url = it.ad.ctaUri.replaceFirst("microsoft-edge:", ""),
                description = it.ad.description
            )
        }
    }

    override suspend fun getRandomWallpaperUrl(): String {
        val country = getQuery("country")
        return api.getLatest(country = country, locale = "en-${country}").ad.portraitImage.asset
    }
}