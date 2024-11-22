package com.example.androidprojecttirzhanov

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
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
import androidx.room.Room
import com.example.androidprojecttirzhanov.data.IndicatorState
import com.example.androidprojecttirzhanov.data.local.AppDatabase
import com.example.androidprojecttirzhanov.data.local.FavoriteUserDao

class MainActivity : ComponentActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var indicatorState: IndicatorState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = ApiServiceBuilder.create(this)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
        val favoriteUserDao = database.favoriteUserDao()
        val repository = UserRepositoryImpl(apiService, favoriteUserDao)

        indicatorState = IndicatorState()
        userViewModel = UserViewModel(repository, this, indicatorState)

//        val factory = UserViewModelFactory(repository, applicationContext)
//        val userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        setContent {
            val navController = rememberNavController()
            MyFootballAppTheme {
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) } // Добавляем BottomNavigationBar
                ) { innerPadding ->
                    NavigationGraph(
                        navController = navController,
                        userViewModel = userViewModel,
                        indicatorState = indicatorState,
                        modifier = Modifier.padding(innerPadding) // Обеспечиваем отступ для навигации
                    )
                }
            }
        }
    }

    // Создание канала уведомлений для Android 8.0 и выше
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "time_channel",
                "Time Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for time-based events"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}