package com.bnyro.wallpaper.ui.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnyro.wallpaper.api.wh.WhApi
import com.bnyro.wallpaper.obj.Wallpaper
import kotlinx.coroutines.launch

class MainModel : ViewModel() {
    var api = WhApi()
    var wallpapers by mutableStateOf(
        listOf<Wallpaper>()
    )

    private var page: Int = 1

    fun fetchWallpapers() {
        viewModelScope.launch {
            wallpapers = wallpapers + api.getWallpapers(page)
            page += 1
        }
    }

    fun clearWallpapers() {
        wallpapers = listOf()
    }
}
