package com.example.androidprojecttirzhanov.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.androidprojecttirzhanov.presentation.viewmodel.UserViewModel


@Composable
fun FavoriteScreen(viewModel: UserViewModel) {
    val favoriteUsers by viewModel.favoriteUsers.collectAsState()

    Column {
        Text("Избранное", style = TextStyle(fontSize = 24.sp), modifier = Modifier.padding(8.dp))

        LazyColumn {
            items(favoriteUsers) { user ->
                Text(
                    text = user.name,
                    style = TextStyle(fontSize = 20.sp),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}