package com.bnyro.wallpaper.ui.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.ui.components.ShimmerGrid
import com.bnyro.wallpaper.ui.components.WallpaperGrid
import com.bnyro.wallpaper.ui.components.WallpaperPageView
import com.bnyro.wallpaper.ui.components.dialogs.FilterDialog
import com.bnyro.wallpaper.ui.models.MainModel
import kotlinx.coroutines.CancellationException
import kotlin.random.Random

private val searchKeywords = listOf(
    "All",
    "Cartoon",
    "Hero",
    "Movie",
    "Nature",
    "Space",
    "Cars",
    "Minimal"
)

@Composable
fun WallpaperPage(
    viewModel: MainModel
) {
    val context = LocalContext.current
    var showFilterDialog by remember {
        mutableStateOf(false)
    }
    var showSearchDialog by remember {
        mutableStateOf(false)
    }
    var searchQuery by remember(viewModel.api.route) {
        mutableStateOf(viewModel.api.selectedTags.joinToString(" "))
    }

    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    var fetchedWallpapers by rememberSaveable {
        mutableStateOf(false)
    }

    val supportsSearch = viewModel.api.supportsTags

    fun fetchMoreWallpapers() {
        viewModel.fetchWallpapers { exception, responsibleApi ->
            // suppress errors when a request is cancelled by user navigation
            if (exception !is CancellationException) {
                Toast.makeText(
                    context,
                    "${responsibleApi.name}: ${exception.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun applySearch(query: String) {
        val selectedTags = query
            .trim()
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }
        viewModel.api.selectedTags = selectedTags
        searchQuery = selectedTags.joinToString(" ")
        selectedIndex = null
        viewModel.clearWallpapers()
        fetchMoreWallpapers()
    }

    LaunchedEffect(Unit) {
        // prevent loading the same page a second time
        if (fetchedWallpapers) return@LaunchedEffect

        viewModel.clearWallpapers()
        fetchMoreWallpapers()

        fetchedWallpapers = true
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BrowseActions(
            query = searchQuery,
            supportsSearch = supportsSearch,
            hasAdvancedFilters = viewModel.api.availableFilters.isNotEmpty() || viewModel.api.requiresCommunityName,
            onClickSearch = {
                showSearchDialog = true
            },
            onClickRandom = {
                selectedIndex = if (viewModel.wallpapers.isNotEmpty()) {
                    Random.nextInt(viewModel.wallpapers.size)
                } else {
                    null
                }
            },
            onClickFilter = {
                showFilterDialog = true
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (viewModel.wallpapers.isNotEmpty()) {
                WallpaperGrid(
                    viewModel = viewModel,
                    wallpapers = viewModel.wallpapers,
                    isLoadingMore = viewModel.isLoadingWallpapers,
                    onClickWallpaper = {
                        selectedIndex = it
                    }
                ) {
                    fetchMoreWallpapers()
                }
                if (viewModel.isLoadingWallpapers) {
                    LoadMoreIndicator(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp)
                    )
                }
                selectedIndex?.let {
                    WallpaperPageView(initialPage = it, wallpapers = viewModel.wallpapers) {
                        selectedIndex = null
                    }
                }
            } else {
                ShimmerGrid()
            }
        }
    }

    if (showFilterDialog) {
        FilterDialog(
            api = viewModel.api
        ) { changed ->
            showFilterDialog = false
            if (changed) {
                searchQuery = viewModel.api.selectedTags.joinToString(" ")
                viewModel.clearWallpapers()
                fetchMoreWallpapers()
            }
        }
    }

    if (showSearchDialog && supportsSearch) {
        SearchDialog(
            query = searchQuery,
            onDismissRequest = {
                showSearchDialog = false
            },
            onApplySearch = {
                showSearchDialog = false
                applySearch(it)
            }
        )
    }
}

@Composable
private fun BrowseActions(
    query: String,
    supportsSearch: Boolean,
    hasAdvancedFilters: Boolean,
    onClickSearch: () -> Unit,
    onClickRandom: () -> Unit,
    onClickFilter: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (supportsSearch) {
                OutlinedButton(
                    onClick = onClickSearch,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Search, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                        Text(
                            text = query.ifBlank { stringResource(R.string.all_wallpapers) },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            FilledTonalIconButton(
                onClick = onClickRandom
            ) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = stringResource(R.string.random_wallpaper)
                )
            }

            if (hasAdvancedFilters || supportsSearch) {
                OutlinedIconButton(
                    onClick = onClickFilter
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = stringResource(R.string.filter)
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadMoreIndicator(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp
            )
            Text(
                text = stringResource(R.string.loading_more_wallpapers),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun SearchDialog(
    query: String,
    onDismissRequest: () -> Unit,
    onApplySearch: (String) -> Unit
) {
    var searchQuery by remember(query) {
        mutableStateOf(query)
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onApplySearch(searchQuery)
                }
            ) {
                Text(stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(stringResource(android.R.string.cancel))
            }
        },
        title = {
            Text(stringResource(R.string.search_wallpapers))
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(stringResource(R.string.keywords))
                    },
                    placeholder = {
                        Text(stringResource(R.string.search_hint))
                    },
                    singleLine = true
                )
                Text(
                    text = stringResource(R.string.popular_keywords),
                    style = MaterialTheme.typography.labelLarge
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchKeywords) { keyword ->
                        ElevatedAssistChip(
                            onClick = {
                                searchQuery = if (keyword.equals("All", ignoreCase = true)) {
                                    ""
                                } else {
                                    keyword
                                }
                            },
                            label = {
                                Text(keyword)
                            }
                        )
                    }
                }
            }
        }
    )
}
