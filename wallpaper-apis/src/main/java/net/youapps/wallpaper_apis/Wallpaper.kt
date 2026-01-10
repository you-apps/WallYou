package net.youapps.wallpaper_apis

data class Wallpaper(
    val imgSrc: String = "",
    val title: String? = null,
    val url: String? = null,
    var author: String? = null,
    val category: String? = null,
    val resolution: String? = null,
    val fileSize: Long? = null,
    val thumb: String? = null,
    val creationDate: String? = null,
    val description: String? = null,
) {
    val preview get() = thumb ?: imgSrc
}
