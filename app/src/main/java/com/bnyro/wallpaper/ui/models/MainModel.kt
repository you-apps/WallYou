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
    private val api = WhApi()
    var wallpapers by mutableStateOf(
        listOf<Wallpaper>()
    )

    fun fetchWallpapers() {
        viewModelScope.launch {
            wallpapers = api.getWallpapers()
        }
    }
}
