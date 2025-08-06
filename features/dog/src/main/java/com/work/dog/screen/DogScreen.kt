package com.work.dog.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.work.dog_service.data.model.DogData
import com.work.dog_service.data.model.response.DogResponse
import org.koin.androidx.compose.koinViewModel

@Composable
fun DogScreen(
    viewModel: DogScreenViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    DogScreen(
        uiState = uiState.value,
        {
            viewModel.onUIEvent(DogScreenViewModel.UIEvent.SequenceReload)
        },
        {
            viewModel.onUIEvent(DogScreenViewModel.UIEvent.ConcurrentReload)
        },
    )
}

@Composable
fun DogScreen(
    uiState: DogScreenViewModel.UIState,
    onSequenceClick: () -> Unit = {},
    onConcurrentClick: () -> Unit = {}
) {
    DogScreenContent(
        uiState = uiState,
        onSequenceClick = onSequenceClick,
        onConcurrentClick = onConcurrentClick
    )
}

@Composable
fun DogScreenContent(
    uiState: DogScreenViewModel.UIState,
    onSequenceClick: () -> Unit = {},
    onConcurrentClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    modifier = Modifier.width(150.dp),
                    onClick = onConcurrentClick,
                    colors = ButtonColors(
                        containerColor = Color(red = 0, green = 170, blue = 255),
                        contentColor = Color.White,
                        disabledContentColor = Color.White,
                        disabledContainerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(size = 8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    border = BorderStroke(width = 2.dp, color = Color.Black),
                ) {
                    Text(
                        "Concurrent Reload",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    modifier = Modifier.width(150.dp),
                    onClick = onSequenceClick,
                    colors = ButtonColors(
                        containerColor = Color(red = 0, green = 170, blue = 255),
                        contentColor = Color.White,
                        disabledContentColor = Color.White,
                        disabledContainerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(size = 8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    border = BorderStroke(width = 2.dp, color = Color.Black),
                ) {
                    Text(
                        "Sequential Reload",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(it)
                .padding(20.dp)
        ) {
            Text(
                text = "Dogs",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(
                modifier = Modifier.height(24.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                itemsIndexed(uiState.dogList) { index, item ->
                    DogScreenItem(
                        index = index,
                        dogData = item
                    )
                }
            }
        }
    }
}

@Composable
fun DogScreenItem(
    index: Int,
    dogData: DogData?
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 5.dp)
    ) {
        AsyncImage(
            model = dogData?.dogResponse?.message,
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 5.dp)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Dog#${index + 1} @ ${dogData?.timeStamp}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DogScreenItemPreview() {
    DogScreenItem(
        index = 0,
        dogData = DogData(dogResponse = DogResponse(message = "1234", status = "success"), timeStamp = "1234")
    )
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