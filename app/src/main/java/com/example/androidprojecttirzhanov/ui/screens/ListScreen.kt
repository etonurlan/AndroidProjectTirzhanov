package com.example.androidprojecttirzhanov.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidprojecttirzhanov.data.FootballerData
import com.example.androidprojecttirzhanov.ui.screens.Typography

@Composable
fun ListScreen(navController: NavController) {
    val footballers = FootballerData.footballers

    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        items(footballers) { footballer ->
            Text(
                text = footballer.name,
                style = Typography.h5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        navController.navigate("detail_screen/${footballer.name}")
                    }
            )
        }
    }
}
