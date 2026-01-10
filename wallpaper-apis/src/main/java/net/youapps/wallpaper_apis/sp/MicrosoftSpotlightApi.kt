package net.youapps.wallpaper_apis.sp

import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.Wallpaper
import net.youapps.wallpaper_apis.WallpaperApi
import net.youapps.wallpaper_apis.sp.obj.SpotlightImage
import java.util.Locale

// Credits to https://github.com/ORelio/Spotlight-Downloader/blob/master/SpotlightAPI.md

class MicrosoftSpotlightApi : WallpaperApi() {
    override val name = "Spotlight"
    override val baseUrl = "https://fd.api.iris.microsoft.com"

    override val availableFilters: Map<String, List<String>> = mapOf(
        "country" to listOf("US") + Locale.getISOCountries().toList().filter { it != "US" }
            .sorted(),
        "orientation" to listOf("portrait", "landscape")
    )

    private val api = RetrofitHelper.create<Spotlight>(baseUrl)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        val country = selectedFilters["country"]!!
        val apiResp = api.getLatestPage(country = country, locale = "en-${country}")
        val images = apiResp.batchrsp.items.map {
            RetrofitHelper.json.decodeFromString<SpotlightImage>(it.item)
        }
        return images.map {
            Wallpaper(
                title = it.ad.title,
                imgSrc = when (selectedFilters["orientation"]) {
                    "portrait" -> it.ad.portraitImage.asset
                    else -> it.ad.landscapeImage.asset
                },
                author = it.ad.copyright,
                url = it.ad.ctaUri.replaceFirst("microsoft-edge:", ""),
                description = it.ad.description
            )
        }
    }

    override suspend fun getRandomWallpaperUrl(): String {
        val country = selectedFilters["country"]!!
        return api.getLatest(country = country, locale = "en-${country}").ad.portraitImage.asset
    }
}