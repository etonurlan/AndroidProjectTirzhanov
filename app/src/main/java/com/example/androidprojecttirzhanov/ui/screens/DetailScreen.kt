package com.example.androidprojecttirzhanov.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androidprojecttirzhanov.data.FootballerData

@Composable
fun DetailScreen(footballerName: String?) {
    val footballer = FootballerData.footballers.find { it.name == footballerName }

    if (footballer != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Name: ${footballer.name}", style = Typography.h5, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Position: ${footballer.position}", style = Typography.h5, color = Color.White)
            Text(text = "Goals: ${footballer.goals}", style = Typography.h5, color = Color.White)
            Text(text = "Team: ${footballer.team}", style = Typography.h5, color = Color.White)
            Text(text = "Height: ${footballer.height} m", style = Typography.h5, color = Color.White)
            Text(text = "Weight: ${footballer.weight} kg", style = Typography.h5, color = Color.White)
        }
    } else {
        Text(text = "Footballer not found", color = Color.White)
    }
}
