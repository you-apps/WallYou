package com.bnyro.wallpaper.ui.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnyro.wallpaper.App
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.db.DatabaseHolder
import com.bnyro.wallpaper.db.DatabaseHolder.Database
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.enums.ThemeMode
import com.bnyro.wallpaper.ui.nav.DrawerScreens
import com.bnyro.wallpaper.util.Either
import com.bnyro.wallpaper.util.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainModel : ViewModel() {
    private val themeModeIndex = Preferences.getString(
        Preferences.themeModeKey,
        ThemeMode.AUTO.value.toString()
    ).toInt()
    var themeMode by mutableStateOf(ThemeMode.values()[themeModeIndex])

    var currentDestination: DrawerScreens by mutableStateOf(DrawerScreens.apiScreens.first())
    var api = App.apis.first()

    var wallpapers by mutableStateOf(
        listOf<Wallpaper>()
    )

    val favWallpapers: StateFlow<List<Wallpaper>> =
        DatabaseHolder.Database.favoritesDao().getFavoritesFlow().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = listOf()
        )

    val recentlyAppliedWallpapers: StateFlow<List<Wallpaper>> =
        DatabaseHolder.Database.favoritesDao().getHistoryFlow().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = listOf()
        )

    var titleResource by mutableStateOf<Either<Int, String>>(Either.Left(R.string.app_name))

    var page: Int = 1

    fun fetchWallpapers(onException: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                wallpapers += withContext(Dispatchers.IO) {
                    api.getWallpapers(page)
                }
                page += 1
            } catch (e: Exception) {
                Log.e(this.javaClass.name, e.toString())
                onException.invoke(e)
            }
        }
    }

    fun addToFavorites(wallpaper: Wallpaper) = viewModelScope.launch(Dispatchers.IO) {
        Database.favoritesDao().insert(wallpaper, true, null)
    }

    fun removeFromFavorites(wallpaper: Wallpaper) = viewModelScope.launch(Dispatchers.IO) {
        Database.favoritesDao().removeFromFavorites(wallpaper)
    }

    fun removeRecentlyAppliedWallpaper(wallpaper: Wallpaper) = viewModelScope.launch(Dispatchers.IO) {
        Database.favoritesDao().removeFromHistory(wallpaper)
    }

    fun clearWallpapers() {
        wallpapers = listOf()
        page = 1
    }
}
