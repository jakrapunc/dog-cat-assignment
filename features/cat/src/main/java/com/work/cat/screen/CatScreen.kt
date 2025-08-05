package com.work.cat.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.Red)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(uiState.breeds?.data ?: emptyList()) { item ->
                Text(
                    text = item.breed
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCatScreen() {
    CatScreen(
        uiState = CatScreenViewModel.UIState()
    )
}