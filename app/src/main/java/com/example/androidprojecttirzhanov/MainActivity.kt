package com.example.androidprojecttirzhanov

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.androidprojecttirzhanov.ui.NavigationGraph
import com.example.androidprojecttirzhanov.ui.components.BottomNavigationBar
import com.example.androidprojecttirzhanov.ui.screens.MyFootballAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyFootballAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) {
                    NavigationGraph(navController)
                }
            }
        }
    }
}

