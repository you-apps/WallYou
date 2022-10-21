package com.bnyro.wallpaper.ui.models

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
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

    val handler = Handler(Looper.getMainLooper())

    private var page: Int = 1

    fun fetchWallpapers(onException: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                wallpapers = wallpapers + api.getWallpapers(page)
                page += 1
            } catch (e: Exception) {
                Log.e(this.javaClass.name, e.toString())
                onException.invoke(e)
            }
        }
    }

    fun clearWallpapers() {
        wallpapers = listOf()
        page = 1
    }
}
