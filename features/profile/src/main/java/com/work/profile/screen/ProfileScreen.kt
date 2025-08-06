package com.work.profile.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.work.base.compose.component.BottomButton
import com.work.base.compose.component.LoadingScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreen(
        uiState = uiState.value,
        onUIEvent = viewModel::onUIEvent
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileScreenViewModel.UIState,
    onUIEvent: (ProfileScreenViewModel.UIEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        if (uiState.isLoading) {
            LoadingScreen()
        } else {
            ProfileContent(
                uiState = uiState,
                onUIEvent = onUIEvent
            )
        }
    }
}

@Composable
fun ProfileContent(
    uiState: ProfileScreenViewModel.UIState,
    onUIEvent: (ProfileScreenViewModel.UIEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BottomButton(
                    text = "Reload Profile",
                    onClick = { onUIEvent(ProfileScreenViewModel.UIEvent.Reload) }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxWidth()
                .background(Color.White)
                .padding(padding)
        ) {
            Text(
                text = "Me",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 36.dp),
            ) {
                item(key = "image") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            modifier = Modifier.size(200.dp)
                                .clip(CircleShape),
                            model = uiState.profile?.picture?.large,
                            placeholder = painterResource(id = com.work.design.R.drawable.profile_sample),
                            contentDescription = null,
                        )
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier.height(36.dp)
                    )
                }
                item(contentType = "item", key = "title") {
                    ProfileItem(
                        header = "Title",
                        value = uiState.profile?.name?.title?: ""
                    )
                }
                item(contentType = "item", key = "firstName") {
                    ProfileItem(
                        header = "Firstname",
                        value = uiState.profile?.name?.first?: ""
                    )
                }
                item(contentType = "item", key = "lastName") {
                    ProfileItem(
                        header = "Lastname",
                        value = uiState.profile?.name?.last?: ""
                    )
                }
                item(contentType = "item", key = "dob") {
                    ProfileItem(
                        header = "Date of Birth",
                        value = uiState.profile?.dob?.date ?: ""
                    )
                }
                item(contentType = "item", key = "age") {
                    ProfileItem(
                        header = "Age",
                        value = uiState.profile?.dob?.age?.toString() ?: ""
                    )
                }
                item(contentType = "gender", key = "gender") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Gender:",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Icon(
                            modifier = Modifier.size(14.dp),
                            painter = painterResource(
                                id = if (uiState.profile?.gender == "female") {
                                    com.work.design.R.drawable.female_gender
                                } else {
                                    com.work.design.R.drawable.male_gender
                                }
                            ),
                            contentDescription = "Gender",
                            tint = Color.Black
                        )
                    }
                }
                item(contentType = "item", key = "nation") {
                    ProfileItem(
                        header = "Nationality",
                        value = uiState.profile?.nat ?: ""
                    )
                }
                item(contentType = "item", key = "mobile") {
                    ProfileItem(
                        header = "Mobile",
                        value = uiState.profile?.cell ?: ""
                    )
                }
                item(contentType = "address", key = "address") {
                    Column() {
                        ProfileItem(
                            header = "Address",
                            value = "${uiState.profile?.location?.street?.number ?: ""} ${uiState.profile?.location?.street?.name ?: ""}"
                        )
                        Text(
                            modifier = Modifier.padding(start = 36.dp),
                            text = uiState.profile?.location?.city ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.padding(start = 36.dp),
                            text = uiState.profile?.location?.state ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.padding(start = 36.dp),
                            text = "${uiState.profile?.location?.country ?: ""} ${uiState.profile?.location?.postcode ?: ""}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileItem(
    header: String,
    value: String
) {
    Text(
        text = "$header: $value",
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        uiState = ProfileScreenViewModel.UIState(
            profile = null,
            isLoading = false,
            error = null
        ),
        onUIEvent = {}
    )
}