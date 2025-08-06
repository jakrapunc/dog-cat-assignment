package com.work.cnd

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onNavigate: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
    ) {
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                delay(2000)
                onNavigate()
            }
        }

        Image(
            modifier = Modifier.size(200.dp)
                .align(Alignment.Center),
            painter = painterResource(id = com.work.design.R.drawable.pet),
            contentDescription = "",
        )
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}