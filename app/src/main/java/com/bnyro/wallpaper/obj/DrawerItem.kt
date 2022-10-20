package com.bnyro.wallpaper.obj

import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit = {}
)
