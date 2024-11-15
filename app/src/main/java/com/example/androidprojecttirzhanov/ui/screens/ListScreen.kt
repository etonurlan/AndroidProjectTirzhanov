package com.example.androidprojecttirzhanov.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.androidprojecttirzhanov.data.IndicatorState
import com.example.androidprojecttirzhanov.presentation.viewmodel.UserViewModel

@Composable
fun ListScreen(viewModel: UserViewModel, navController: NavController, indicatorState: IndicatorState) {
    val users by viewModel.users.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val favoriteUsers by viewModel.favoriteUsers.collectAsState()
    val isFilterActive by indicatorState.isFilterActive.collectAsState()

    Box {
        if (loading) {
            CircularProgressIndicator()
        } else {
            Column(modifier = Modifier.align(Alignment.TopEnd)) {
                if (isFilterActive) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color.Blue, shape = CircleShape)
                    )
                }
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Indicator",
                    tint = Color.Black
                )
            }

            LazyColumn(
                modifier = Modifier.padding(top = 20.dp)
            ) {
                items(users) { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                navController.navigate("detail_screen/${user.id}")
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = user.name,
                            style = TextStyle(fontSize = 20.sp),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite Icon",
                            tint = if (favoriteUsers.any { it.id == user.id }) Color.Red else Color.Gray,
                            modifier = Modifier.clickable {
                                viewModel.toggleFavoriteUser(user)
                            }
                        )
                    }
                }
            }
        }
    }
}
