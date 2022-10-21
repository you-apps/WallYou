package com.bnyro.wallpaper.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.api.wh.WhApi
import com.bnyro.wallpaper.obj.DrawerItem
import com.bnyro.wallpaper.ui.components.NavigationDrawer
import com.bnyro.wallpaper.ui.models.MainModel
import com.bnyro.wallpaper.ui.nav.AppNavHost
import com.bnyro.wallpaper.ui.theme.WallYouTheme
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainModel = ViewModelProvider(this).get()
        
        showContent {
            MainContent()
        }

        viewModel.fetchWallpapers {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent() {
    val context = LocalContext.current
    val viewModel: MainModel = viewModel()
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    NavigationDrawer(
        drawerState = drawerState,
        items = listOf(
            DrawerItem(
                "Wallhaven",
                ImageVector.vectorResource(R.drawable.ic_wallhaven)
            ) {
                navController.navigate("Wallhaven")

                viewModel.api = WhApi()
                viewModel.clearWallpapers()

                viewModel.fetchWallpapers {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            },
            DrawerItem(
                stringResource(R.string.favorites),
                Icons.Default.Favorite,
                true
            ),
            DrawerItem(
                stringResource(R.string.settings),
                Icons.Default.Settings,
                true
            ),
            DrawerItem(
                stringResource(R.string.about),
                Icons.Default.Info
            ) {
                navController.navigate("About")
            }
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.app_name)
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
        MainContent()
    }
}
