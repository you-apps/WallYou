package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    items: List<String>,
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    itemHeight: Dp = 48.dp,
    textStyle: TextStyle = LocalTextStyle.current,
    dividerColor: Color = MaterialTheme.colorScheme.primary,
    onItemSelected: (Int) -> Unit,
) {
    val pagerState =
        rememberPagerState(initialPage = if (startIndex > 0) startIndex else 0) { items.size }

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    Box(modifier = modifier) {
        VerticalPager(
            state = pagerState,
            contentPadding = PaddingValues(vertical = itemHeight * (visibleItemsCount / 2)),
            pageSize = PageSize.Fixed(itemHeight),
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient),
            beyondViewportPageCount = visibleItemsCount / 2,
        ) { page ->
            val item = items[page]

            val isSelected = page == pagerState.currentPage
            val scale = if (isSelected) 1.2f else 0.8f

            Box(
                modifier = Modifier.height(itemHeight)
            ) {
                Text(
                    text = item,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    modifier = textModifier
                        .align(Alignment.Center)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .padding(vertical = 8.dp),
                )
            }
        }

        HorizontalDivider(
            color = dividerColor,
            thickness = 2.dp,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = -(itemHeight / 2))
        )

        HorizontalDivider(
            color = dividerColor,
            thickness = 2.dp,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = itemHeight / 2)
        )
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onItemSelected(page)
        }
    }
}

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }
