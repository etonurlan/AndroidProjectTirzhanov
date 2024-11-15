package com.example.androidprojecttirzhanov.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.getValue

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf("list_screen", "settings_screen", "favorite_screen")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Settings, Icons.Default.Star)
    BottomNavigation(
        backgroundColor = Color.Blue,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEachIndexed { index, screen ->
            BottomNavigationItem(
                icon = { Icon(icons[index], contentDescription = null) },
                label = {
                    Text(
                        text = when (screen) {
                            "list_screen" -> "Footballers"
                            "settings_screen" -> "Settings"
                            "favorite_screen" -> "Favorites"
                            else -> ""
                        }
                    )
                },
                selected = currentRoute == screen,
                onClick = {
                    navController.navigate(screen) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
