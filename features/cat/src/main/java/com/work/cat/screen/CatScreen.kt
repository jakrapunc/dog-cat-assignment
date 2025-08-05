package com.work.cat.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.work.base.compose.component.LoadingScreen
import com.work.cat_service.data.model.CatBreedItem
import com.work.cat_service.data.model.response.CatBreedsResponse
import org.koin.androidx.compose.koinViewModel

@Composable
fun CatScreen(
    viewModel: CatScreenViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    CatScreen(
        uiState = uiState.value
    )
}

@Composable
fun CatScreen(
    uiState: CatScreenViewModel.UIState,
) {
    Scaffold() {
        if (uiState.isLoading) {
            LoadingScreen()
        } else {
            CatScreenContent(
                modifier = Modifier.padding(it),
                uiState = uiState
            )
        }
    }
}

@Composable
fun CatScreenContent(
    modifier: Modifier = Modifier,
    uiState: CatScreenViewModel.UIState,
) {
    var currentExpand by rememberSaveable { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Cats",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black
                ),
        ) {
            item(key = "header", contentType = "header") {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        ),
                    text = "Cat Breeds",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Black)
                )
            }
            itemsIndexed(
                items = uiState.breeds?.data ?: emptyList(),
                key = { index, _ -> index },
                contentType = { _, _ -> "breed_item" }
            ) { index, item ->
                BreedItem(
                    isExpanded = currentExpand == index,
                    item = item
                ) {
                    currentExpand = if (currentExpand == index) {
                        null
                    } else {
                        index
                    }
                }
            }
        }
    }
}

@Composable
fun BreedItem(
    isExpanded: Boolean,
    item: CatBreedItem,
    onClick: () -> Unit,
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "arrowRotation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 5.dp
            )
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.breed,
            style = MaterialTheme.typography.bodyLarge,
        )
        IconButton(
            modifier = Modifier.size(24.dp),
            onClick = onClick,
        ) {
            Icon(
                modifier = Modifier.rotate(rotationAngle),
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "",
                tint = Color.Black
            )
        }
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.Black)
    )

    AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(durationMillis = 200)
        ),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(durationMillis = 200)
        )
    ) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 40.dp,
                        vertical = 5.dp
                    )
            ) {
                Text(
                    text = "Country: ${item.country}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Origin: ${item.origin}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Coat: ${item.coat}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Pattern: ${item.pattern}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Black)
            )
        }
    }
}

@Preview
@Composable
fun PreviewCatScreen() {
    CatScreen(
        uiState = CatScreenViewModel.UIState(
            breeds = CatBreedsResponse(
                data = listOf(
                    CatBreedItem(
                        breed = "Breed 1",
                        origin = "Origin 1",
                        country = "Country 1",
                        coat = "Coat 1",
                        pattern = "Pattern 1",
                    ),
                    CatBreedItem(
                        breed = "Breed 2",
                        origin = "Origin 1",
                        country = "Country 1",
                        coat = "Coat 1",
                        pattern = "Pattern 1",
                    ),
                    CatBreedItem(
                        breed = "Breed 3",
                        origin = "Origin 1",
                        country = "Country 1",
                        coat = "Coat 1",
                        pattern = "Pattern 1",
                    )
                )
            )
        )
    )
}