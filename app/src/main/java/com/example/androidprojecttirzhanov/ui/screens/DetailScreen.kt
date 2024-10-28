package com.example.androidprojecttirzhanov.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidprojecttirzhanov.presentation.viewmodel.UserViewModel

@Composable
fun DetailScreen(
    userId: Int,
    navController: NavController,
    viewModel: UserViewModel
) {
    val user = viewModel.users.collectAsState().value.find { it.id == userId }

    user?.let {
        Text(
            text = "Name: ${it.name}\nUsername: ${it.username}\nEmail: ${it.email}",
            style = TextStyle(fontSize = 20.sp) // Укажите нужный размер шрифта
        )
    } ?: Text(
        text = "User not found",
        style = TextStyle(fontSize = 20.sp) // Также увеличиваем шрифт для сообщения об ошибке
    )
}