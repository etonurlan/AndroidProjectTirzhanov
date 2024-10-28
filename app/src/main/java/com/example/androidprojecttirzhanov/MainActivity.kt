package com.example.androidprojecttirzhanov

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.androidprojecttirzhanov.ui.NavigationGraph
import com.example.androidprojecttirzhanov.data.repository.UserRepositoryImpl
import com.example.androidprojecttirzhanov.presentation.viewmodel.UserViewModelFactory
import com.example.androidprojecttirzhanov.presentation.viewmodel.UserViewModel
import com.example.androidprojecttirzhanov.data.remote.ApiServiceBuilder
import com.example.androidprojecttirzhanov.ui.components.BottomNavigationBar
import com.example.androidprojecttirzhanov.ui.screens.MyFootballAppTheme
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = ApiServiceBuilder.create(this)
        val repository = UserRepositoryImpl(apiService)
        val factory = UserViewModelFactory(repository)
        val userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

//        setContent {
//            NavigationGraph(navController = rememberNavController(), userViewModel = userViewModel)
//        }
        setContent {
            val navController = rememberNavController()
            MyFootballAppTheme {
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) } // Добавляем BottomNavigationBar
                ) { innerPadding ->
                    NavigationGraph(
                        navController = navController,
                        userViewModel = userViewModel,
                        modifier = Modifier.padding(innerPadding) // Обеспечиваем отступ для навигации
                    )
                }
            }
        }
    }
}