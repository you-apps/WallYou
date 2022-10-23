package com.bnyro.wallpaper.ext

fun Long.formatMinutes(): String {
    val timeParts = mutableListOf<String>()
    val days = this / 1440L
    val hours = this % 1440L / 60L
    val minutes = this % 1440L % 60L
    if (days != 0L) timeParts += "$days day" + if (days != 1L) "s" else ""
    if (hours != 0L) timeParts += "$hours hour" + if (hours != 1L) "s" else ""
    if (minutes != 0L) timeParts += "$minutes minute" + if (minutes != 1L) "s" else ""
    return timeParts.joinToString(" ")
}
