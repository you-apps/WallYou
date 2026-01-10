package com.bnyro.wallpaper.util

import androidx.compose.ui.graphics.vector.ImageVector
import net.youapps.wallpaper_apis.Wallpaper
import net.youapps.wallpaper_apis.WallpaperApi

/**
 * A wrapper around [WallpaperApi] that uses [Preferences] to store the engine settings.
 */
class WallpaperApiWrapper(private val api: WallpaperApi, val icon: ImageVector): WallpaperApi() {
    val route get() = api.name.replace(" ", "_").lowercase()

    // Delegated to underlying [api]
    override val name: String get() = api.name
    override val baseUrl: String get() = api.baseUrl
    override val availableFilters: Map<String, List<String>> get() = api.availableFilters
    override val supportsTags: Boolean get() = api.supportsTags
    override val requiresCommunityName: Boolean get() = api.requiresCommunityName

    override suspend fun getWallpapers(page: Int): List<Wallpaper> = api.getWallpapers(page)
    override suspend fun getRandomWallpaperUrl(): String? = api.getRandomWallpaperUrl()

    init {
        // copy all values from the [WallpaperApiWrapper] to the underlying [WallpaperApi] instance
        api.communityName = communityName
        api.selectedTags = selectedTags
        api.selectedFilters = selectedFilters
    }

    // Engine settings that automatically get stored to [Preferences]
    override var communityName: String?
        get() = getPref("community", api.communityName.orEmpty())
        set(value) {
            setPref("community", name)
            api.communityName = value
        }

    override var selectedFilters: Map<String, String>
        get() = run {
            val keys = api.availableFilters.keys

            keys.associateWith { key ->
                getPref(key, api.selectedFilters[key].orEmpty())
            }
        }
        set(filters) {
            for ((key, value) in filters.entries) {
                setPref(key, value)
            }

            api.selectedFilters = filters
        }

    override var selectedTags: List<String>
        get() = getPref("tags", "").split(",").filter {
            it.isNotBlank()
        }
        set(value) {
            setPref("tags", value.joinToString(","))
            api.selectedTags = value
        }

    private fun getPref(key: String, defValue: String): String {
        return Preferences.getString(api.name + key, defValue)
    }

    private fun setPref(key: String, value: String) {
        Preferences.edit { putString(api.name + key, value) }
    }

    /**
     * Helper Method for changing a single search filter from [availableFilters].
     */
    fun setFilter(key: String, value: String) {
        selectedFilters = selectedFilters.toMutableMap().also {
            it[key] = value
        }
    }
}