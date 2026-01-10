package net.youapps.wallpaper_apis

/**
 * An abstract class representing a Wallpaper API that supports loading a list of wallpapers via [getWallpapers]
 * and loading the URL of a single random wallpaper using [getRandomWallpaperUrl].
 */
abstract class WallpaperApi {
    /**
     * The name of the wallpaper API.
     */
    abstract val name: String

    /**
     * The base URL of the wallpaper API.
     */
    abstract val baseUrl: String

    /**
     * A map of the supported filter keys to a list of all its possible options.
     */
    open val availableFilters: Map<String, List<String>> = mapOf()

    /**
     * Whether the API supports searching by tags, i.e. a search query.
     */
    open val supportsTags: Boolean = false

    /**
     * Whether the API requires a [communityName] to be set in order to work.
     */
    open val requiresCommunityName: Boolean = false

    // These params can be set from outside to configure the API

    private var _selectedFilters: Map<String, String> = mapOf()
    /**
     * The currently selected search filters.
     *
     * This is guaranteed to contain all keys from [availableFilters] and only values from [availableFilters].
     */
    open var selectedFilters: Map<String, String>
        // HACK: prevent NullPointerException when [availableFilters] gets initialized after [selectedFilters]
        // if removing this, initializing a [WallpaperApi] crashes immediately because [selectedFilters]
        // gets initialized before the [availableFilters] attribute of the inheriting wallpaper class is initialized
        set(value) = run {
            _selectedFilters = value
        }
        get() = run {
            _selectedFilters.ifEmpty { availableFilters.map { (key, values) -> key to values.first() }.toMap() }
        }

    /**
     * The tags / query to use for searching. Only relevant if [supportsTags] is `true`.
     */
    open var selectedTags: List<String> = emptyList()

    /**
     * The name of the community. Only read if [requiresCommunityName] is `true`.
     */
    open var communityName: String? = null

    /**
     * Load a list of wallpapers by respecting. [selectedTags] and [selectedFilters].
     *
     * @param page the page of the search results, starting at 1 (e.g. 1, 2, 3, ...)
     */
    abstract suspend fun getWallpapers(page: Int): List<Wallpaper>

    /**
     * Get the URL of a random wallpaper.
     *
     * This does not always respect [selectedFilters] and [selectedTags] due to API restrictions,
     * but it attempts to respect these filters for generating the URL of a random wallpaper whenever possible.
     */
    abstract suspend fun getRandomWallpaperUrl(): String?
}
