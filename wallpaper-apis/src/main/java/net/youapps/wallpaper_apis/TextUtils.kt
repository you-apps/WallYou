package net.youapps.wallpaper_apis

object TextUtils {
    private val markdownRegex = Regex("[*_~`#]")

    fun removeMarkdownSymbols(text: String) = text.replace(markdownRegex, "")
}