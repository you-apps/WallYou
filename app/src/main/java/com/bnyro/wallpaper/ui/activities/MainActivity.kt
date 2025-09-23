package com.bnyro.wallpaper.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
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
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = viewModel.titleResource.str()
                        )
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
                    }
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WallYouTheme {
        MainContent(viewModel())
    }
}
