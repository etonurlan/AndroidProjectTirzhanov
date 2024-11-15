package com.example.androidprojecttirzhanov.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.androidprojecttirzhanov.presentation.viewmodel.UserViewModel

@Composable
fun ListScreen(viewModel: UserViewModel, navController: NavController) {
    val users by viewModel.users.collectAsState()
    val loading by viewModel.loading.collectAsState()

    if (loading) {
        CircularProgressIndicator()
    } else {
        LazyColumn {
            items(users) { user ->
                Text(
                    text = user.name,
                    style = TextStyle(fontSize = 20.sp),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            navController.navigate("detail_screen/${user.id}")
                        }
                )
            }
        }
    }
}
