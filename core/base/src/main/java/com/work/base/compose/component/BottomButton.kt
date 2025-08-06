package com.work.base.compose.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun BottomButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier.width(150.dp),
        onClick = onClick,
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
            text,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}