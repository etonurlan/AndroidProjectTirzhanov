package com.example.androidprojecttirzhanov.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androidprojecttirzhanov.presentation.viewmodel.UserViewModel

@Composable
fun SettingsScreen(
    userViewModel: UserViewModel,
    onNameFilterChange: (String) -> Unit,
    onEvenIdFilterChange: (Boolean) -> Unit
) {
    val nameFilter by userViewModel.currentNameFilter.collectAsState()
    val onlyEvenIds by userViewModel.currentOnlyEvenIds.collectAsState()




    Column(modifier = Modifier.padding(16.dp)) {
        // Using OutlinedTextField for a cleaner border
        OutlinedTextField(
            value = nameFilter,
            onValueChange = { newValue ->
                userViewModel.setNameFilter(newValue) // сохраняем изменения
                onNameFilterChange(newValue) // вызываем callback
            },
            label = { Text("Search by name") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Card(
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.Gray),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Checkbox(
                    checked = onlyEvenIds, // используем Boolean
                    onCheckedChange = { isChecked ->
                        userViewModel.setOnlyEvenIdsFilter(isChecked) // сохраняем изменения
                        onEvenIdFilterChange(isChecked) // вызываем callback
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Only even IDs")
            }
        }
    }
}