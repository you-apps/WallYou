package com.bnyro.wallpaper.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.enums.ThemeMode
import com.bnyro.wallpaper.ui.components.NavigationDrawer
import com.bnyro.wallpaper.ui.models.MainModel
import com.bnyro.wallpaper.ui.nav.AppNavHost
import com.bnyro.wallpaper.ui.nav.DrawerScreens
import com.bnyro.wallpaper.ui.theme.WallYouTheme
import com.bnyro.wallpaper.util.Preferences
import com.bnyro.wallpaper.util.str
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainModel = ViewModelProvider(this).get()
        enableEdgeToEdge()
        showContent {
            MainContent(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    viewModel: MainModel
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // prevent page refreshes when orientation changes
    var alreadySetStartDestination by rememberSaveable {
        mutableStateOf(false)
    }
    // navigate to the last opened tab (from the previous app session)
    LaunchedEffect(Unit) {
        if (alreadySetStartDestination) return@LaunchedEffect
        alreadySetStartDestination = true

        Log.e("view model", "view model")
        val initialPage = if (viewModel.currentDestination in DrawerScreens.apiScreens) {
            val lastSelectedTab = Preferences.getString(Preferences.startTabKey, "")
            DrawerScreens.screens.firstOrNull { it.route == lastSelectedTab }
        } else {
            viewModel.currentDestination
        }
        initialPage?.route?.runCatching { navController.navigate(this) }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (DrawerScreens.apiScreens.any { it.route == destination.route }) {
                Preferences.edit { putString(Preferences.startTabKey, destination.route) }
            }
        }
    }

    NavigationDrawer(
        drawerState = drawerState,
        navController = navController,
        viewModel = viewModel,
        pages = DrawerScreens.screens
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = viewModel.titleResource.str(),
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = stringResource(destinationSubtitleRes(viewModel.currentDestination)),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                null
                            )
                        }
                    },
                    actions = {
                        if (viewModel.currentDestination in DrawerScreens.apiScreens) {
                            val systemDarkTheme = isSystemInDarkTheme()
                            val isDarkTheme = when (viewModel.themeMode) {
                                ThemeMode.DARK -> true
                                ThemeMode.LIGHT -> false
                                ThemeMode.AUTO -> systemDarkTheme
                            }
                            val toggleIcon = if (isDarkTheme) {
                                Icons.Default.LightMode
                            } else {
                                Icons.Default.DarkMode
                            }
                            val contentDescription = if (isDarkTheme) {
                                stringResource(R.string.switch_to_light)
                            } else {
                                stringResource(R.string.switch_to_dark)
                            }

                            IconButton(
                                onClick = {
                                    val newMode = if (isDarkTheme) {
                                        ThemeMode.LIGHT
                                    } else {
                                        ThemeMode.DARK
                                    }
                                    viewModel.updateThemeMode(newMode)
                                }
                            ) {
                                Icon(
                                    imageVector = toggleIcon,
                                    contentDescription = contentDescription
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        ) {
            AppNavHost(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

private fun destinationSubtitleRes(destination: DrawerScreens): Int {
    return when (destination) {
        DrawerScreens.Companion.Favorites -> R.string.subtitle_favorites
        DrawerScreens.Companion.History -> R.string.subtitle_history
        DrawerScreens.Companion.Settings -> R.string.subtitle_settings
        DrawerScreens.Companion.About -> R.string.subtitle_about
        else -> R.string.subtitle_discover
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WallYouTheme {
        MainContent(viewModel())
    }
}

