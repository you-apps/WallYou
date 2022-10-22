package com.bnyro.wallpaper.ui.components.dialogs

import androidx.compose.runtime.Composable
import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.ui.components.prefs.FilterDialog

@Composable
fun WallhavenFilterDialog(
    api: Api,
    onDismissRequest: (Boolean) -> Unit
) {
    FilterDialog(
        onDismissRequest = onDismissRequest,
        api = api,
        filters = mapOf()
    )
}
