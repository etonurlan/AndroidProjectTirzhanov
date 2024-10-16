package com.example.androidprojecttirzhanov.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidprojecttirzhanov.ui.screens.DetailScreen
import com.example.androidprojecttirzhanov.ui.screens.ListScreen
import com.example.androidprojecttirzhanov.ui.screens.SettingsScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "list_screen") {
        composable("list_screen") {
            ListScreen(navController)
        }
        composable("detail_screen/{footballerName}") { backStackEntry ->
            val footballerName = backStackEntry.arguments?.getString("footballerName")
            DetailScreen(footballerName)
        }
        composable("settings_screen") {
            SettingsScreen()
        }
    }
}
