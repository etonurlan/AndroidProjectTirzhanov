package com.example.androidprojecttirzhanov.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidprojecttirzhanov.presentation.viewmodel.UserViewModel
import com.example.androidprojecttirzhanov.ui.screens.DetailScreen
import com.example.androidprojecttirzhanov.ui.screens.ListScreen
import com.example.androidprojecttirzhanov.ui.screens.SettingsScreen
import androidx.compose.ui.Modifier
import com.example.androidprojecttirzhanov.data.IndicatorState
import com.example.androidprojecttirzhanov.ui.screens.FavoriteScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    userViewModel: UserViewModel,
    indicatorState: IndicatorState,
    modifier: Modifier = Modifier) {


    NavHost(navController = navController, startDestination = "list_screen", Modifier.then(modifier)) {
        composable("list_screen") {
            ListScreen(navController = navController, viewModel = userViewModel, indicatorState = indicatorState)
        }
        composable("detail_screen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
            userId?.let {
                DetailScreen(userId, navController = navController, viewModel = userViewModel)
            }
        }
        composable("settings_screen") {
            SettingsScreen(
                userViewModel = userViewModel,
                onNameFilterChange = { userViewModel.setNameFilter(it) },
                onEvenIdFilterChange = { userViewModel.setOnlyEvenIdsFilter(it) }
            )
        }
        composable("favorite_screen") {
            FavoriteScreen(viewModel = userViewModel)
        }
    }
}
