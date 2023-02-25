package com.example.idea.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.idea.network.Hit


@Preview
@Composable
fun MainContent(viewModel: MainViewModel = hiltViewModel()) {
    val query: MutableState<String> = remember { mutableStateOf("Cat") }
    val result = viewModel.list.value
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(8.dp)) {

            OutlinedTextField(value = query.value, onValueChange = {
                query.value = it
                viewModel.getImageList(query.value)

            }, enabled = true,
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                label = { Text(text = "Search here...") },
                modifier=Modifier.fillMaxWidth()
            )

            AnimatedVisibility(visible = result.isLoading) {
                Box(modifier = Modifier
                    .fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            AnimatedVisibility(visible = result.error.isNotEmpty()) {
                Box(modifier = Modifier
                    .fillMaxSize()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = viewModel.list.value.error
                    )
                }
            }
            var selectedItem by remember{mutableStateOf( "")}
            var selectedIndex by remember{mutableStateOf(-1)}
            val onItemClick = { index: Int ->
                selectedIndex = index
            }

            AnimatedVisibility(visible = result.data.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells
                        .Fixed(2),
                    modifier = Modifier
                ) {
                    viewModel.list.value.data.let {list ->
                        itemsIndexed(list) { index, it ->
                            MainContentItem(
                                hit = it,
                                index = index,
                                onClick = {
                                    viewModel.toggleSelection(index)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainContentItem(hit: Hit, onClick: () -> Unit, index: Int) {
    val isSelected by remember {
        mutableStateOf(false)
    }
    val hitItem = InterestsItem(hit.tags, isSelected, hit.largeImageURL)
    val colorListGradient = listOf(
        Color.White,
        Color.LightGray,
        Color.Black,
    )

    val mainColorListGradient = listOf(
        Color.White,
        Color.LightGray,
        Color.Black,
        Color.White

    )

    val infiniteTransition = rememberInfiniteTransition()
    val borderColor by remember {
        mutableStateOf(colorListGradient)
    }
    val currentFontSizePx = with(LocalDensity.current) { 40.sp.toPx() }
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = currentFontSizePx,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = LinearEasing,
                delayMillis = 0
            )
        )
    )

    val brush = Brush.linearGradient(
        colors = borderColor,
        start = Offset(offset, offset),
        end = Offset(offset + currentFontSizePx, offset + currentFontSizePx),
        tileMode = TileMode.Repeated
    )

    val mainBrush = Brush.linearGradient(
        colors = mainColorListGradient
    )

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, mainBrush, RoundedCornerShape(20.dp))
            .clickable {
                onClick()
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = hitItem.url),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.Center)
        )
        Text(
            text = hitItem.tag,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomStart),
            color = Color.White
        )
    }
}