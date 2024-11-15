package com.example.androidprojecttirzhanov.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidprojecttirzhanov.data.remote.ApiServiceBuilder
import com.example.androidprojecttirzhanov.data.repository.UserRepositoryImpl
import com.example.androidprojecttirzhanov.presentation.viewmodel.UserViewModel
import com.example.androidprojecttirzhanov.presentation.viewmodel.UserViewModelFactory
import com.example.androidprojecttirzhanov.ui.screens.DetailScreen
import com.example.androidprojecttirzhanov.ui.screens.ListScreen
import com.example.androidprojecttirzhanov.ui.screens.SettingsScreen
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun NavigationGraph(navController: NavHostController, userViewModel: UserViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val apiService = ApiServiceBuilder.create(context)
    val userRepository = UserRepositoryImpl(apiService)
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(userRepository))

    NavHost(navController = navController, startDestination = "list_screen", Modifier.then(modifier)) {
        composable("list_screen") {
            ListScreen(navController = navController, viewModel = userViewModel)
        }
        composable("detail_screen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
            userId?.let {
                DetailScreen(userId, navController = navController, viewModel = userViewModel)
            }
        }
        composable("settings_screen") {
            SettingsScreen()
        }
    }
}
