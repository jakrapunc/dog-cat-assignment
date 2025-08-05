package com.work.dog.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.work.dog_service.data.model.DogData
import com.work.dog_service.data.model.response.DogResponse
import org.koin.androidx.compose.koinViewModel

@Composable
fun DogScreen(
    viewModel: DogScreenViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    DogScreen(
        uiState = uiState.value
    )
}

@Composable
fun DogScreen(
    uiState: DogScreenViewModel.UIState
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(uiState.dogList) { item ->
                Text(
                   text = item?.dogResponse?.message ?: "-",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DogScreenPreview() {
    DogScreen(
        uiState = DogScreenViewModel.UIState(
            dogList = listOf(
                DogData(dogResponse = DogResponse(message = "1234", status = "success"), timeStamp = "1234"),
                DogData(dogResponse = DogResponse(message = "1234", status = "success"), timeStamp = "1234"),
                DogData(dogResponse = DogResponse(message = "1234", status = "success"), timeStamp = "1234"),
            ),
            isLoading = false,
            error = null
        )
    )
}