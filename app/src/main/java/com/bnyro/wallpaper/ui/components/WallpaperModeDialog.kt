package com.bnyro.wallpaper.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.db.DatabaseHolder
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.ui.components.dialogs.ListDialog
import com.bnyro.wallpaper.ui.models.WallpaperHelperModel
import com.bnyro.wallpaper.util.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun WallpaperModeDialog(
    wallpaper: Wallpaper,
    wallpaperHelperModel: WallpaperHelperModel,
    onDismissRequest: () -> Unit,
    applyFilter: Boolean = false
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    ListDialog(
        title = stringResource(R.string.set_wallpaper),
        items = listOf(
            stringResource(R.string.both),
            stringResource(R.string.home),
            stringResource(R.string.lockscreen),
            stringResource(id = R.string.set_with)
        ),
        onDismissRequest = onDismissRequest,
        onClick = { index ->
            scope.launch(Dispatchers.IO) {
                if (Preferences.getBoolean(Preferences.wallpaperHistory, true)) {
                    DatabaseHolder.Database.favoritesDao()
                        .insert(wallpaper, null, true)
                }
                withContext(Dispatchers.Main) {
                    if (index == 3) {
                        wallpaperHelperModel.setWallpaperWith(context, wallpaper)
                    } else {
                        if (applyFilter) {
                            wallpaperHelperModel.setWallpaperWithFilter(wallpaper = wallpaper, index)
                        } else {
                            wallpaperHelperModel.setWallpaper(wallpaper = wallpaper, index)
                        }
                    }
                    onDismissRequest.invoke()
                }
            }
        }
    )
}