package com.bnyro.wallpaper.util

import java.text.DateFormat
import java.util.Date
import java.util.Locale

object TextUtils {
    fun formatDate(unixTime: Int): String {
        val formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
        val date = Date(unixTime.toLong() * 1000L)
        return formatter.format(date)
    }
}
