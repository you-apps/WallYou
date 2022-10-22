package com.bnyro.wallpaper.ui.components.dialogs

import androidx.compose.runtime.Composable
import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.ui.components.prefs.FilterDialog
import com.bnyro.wallpaper.util.PrefHolder

@Composable
fun WallhavenFilterDialog(
    api: Api,
    onDismissRequest: () -> Unit
) {
    FilterDialog(
        onDismissRequest = onDismissRequest,
        api = api,
        filters = mapOf()
    )
}
