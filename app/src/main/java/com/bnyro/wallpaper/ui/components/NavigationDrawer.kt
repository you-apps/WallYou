package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.obj.DrawerItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    val socialItems = listOf(
        DrawerItem(
            stringResource(R.string.favorites),
            Icons.Default.Favorite
        ),
        DrawerItem(
            stringResource(R.string.settings),
            Icons.Default.Settings
        ),
        DrawerItem(
            stringResource(R.string.about),
            Icons.Default.Info
        )
    )

    val selectedItem = remember {
        mutableStateOf(socialItems[0])
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                socialItems.forEach {
                    if (socialItems.indexOf(it) == socialItems.size - 2) {
                        Divider(
                            modifier = Modifier
                                .padding(25.dp, 15.dp)
                                .height(2.dp)
                        )
                    }
                    NavigationDrawerItem(
                        icon = {
                            Icon(it.icon, null)
                        },
                        label = {
                            Text(it.title)
                        },
                        selected = it == selectedItem.value,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                it.onClick()
                            }
                            selectedItem.value = it
                        },
                        modifier = Modifier.padding(12.dp, 3.dp)
                    )
                }
            }
        },
        content = content
    )
}

val MinimumDrawerWidth = 240.dp
val MaximumDrawerWidth = 300.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawerSheet(
    windowInsets: WindowInsets,
    modifier: Modifier = Modifier,
    drawerShape: Shape = RectangleShape,
    drawerContainerColor: Color = MaterialTheme.colorScheme.surface,
    drawerContentColor: Color = contentColorFor(drawerContainerColor),
    drawerTonalElevation: Dp = DrawerDefaults.PermanentDrawerElevation,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .sizeIn(
                minWidth = MinimumDrawerWidth,
                maxWidth = DrawerDefaults.MaximumDrawerWidth
            )
            .fillMaxHeight(),
        shape = drawerShape,
        color = drawerContainerColor,
        contentColor = drawerContentColor,
        tonalElevation = drawerTonalElevation
    ) {
        Column(
            Modifier
                .sizeIn(
                    minWidth = MinimumDrawerWidth,
                    maxWidth = MaximumDrawerWidth
                )
                .windowInsetsPadding(windowInsets),
            content = content
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun ModalDrawerSheet(
    modifier: Modifier = Modifier,
    drawerShape: Shape = DrawerDefaults.shape,
    drawerContainerColor: Color = MaterialTheme.colorScheme.surface,
    drawerContentColor: Color = contentColorFor(drawerContainerColor),
    drawerTonalElevation: Dp = DrawerDefaults.ModalDrawerElevation,
    windowInsets: WindowInsets = DrawerDefaults.windowInsets,
    content: @Composable ColumnScope.() -> Unit
) {
    DrawerSheet(
        windowInsets,
        modifier,
        drawerShape,
        drawerContainerColor,
        drawerContentColor,
        drawerTonalElevation,
        content
    )
}
