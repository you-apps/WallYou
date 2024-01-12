package com.bnyro.wallpaper.util

import android.content.Context
import android.content.res.Configuration


object ThemeHelper {
    fun isNightMode(context: Context): Boolean {
        val nightModeFlags =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
}