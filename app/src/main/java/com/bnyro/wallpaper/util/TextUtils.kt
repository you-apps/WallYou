package com.bnyro.wallpaper.util

object TextUtils {
    private val markdownRegex = Regex("[*_~`#]")

    fun removeMarkdownSymbols(text: String) = text.replace(markdownRegex, "")
}
