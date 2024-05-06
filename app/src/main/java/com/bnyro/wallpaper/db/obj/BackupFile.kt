package com.bnyro.wallpaper.db.obj

import kotlinx.serialization.Serializable

@Serializable
data class BackupFile(
    val favorites: List<Wallpaper> = emptyList()
)
