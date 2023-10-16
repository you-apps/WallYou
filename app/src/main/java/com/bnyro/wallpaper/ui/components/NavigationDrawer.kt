package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.ui.nav.DrawerScreens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    pages: List<DrawerScreens>,
    navController: NavController,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    var selectedItem by remember {
        val initialPage = pages.firstOrNull { navController.currentDestination?.route == it.route }
        mutableStateOf(initialPage ?: DrawerScreens.Wallhaven)
    }

    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            pages.firstOrNull { it.route == destination.route }?.let { selectedItem = it }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            val scrollState = rememberScrollState()

            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(scrollState),
            ) {
                Spacer(Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painterResource(R.drawable.ic_launcher_foreground), null)
                    Spacer(Modifier.width(0.dp))
                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 20.sp
                    )
                }
                Spacer(Modifier.height(20.dp))
                pages.forEach {
                    if (it.divideBefore) {
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
                            Text(stringResource(it.titleResource))
                        },
                        selected = it == selectedItem,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                navController.navigate(it.route)
                            }
                            selectedItem = it
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
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
