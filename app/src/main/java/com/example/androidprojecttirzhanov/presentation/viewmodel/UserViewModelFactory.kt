package com.example.androidprojecttirzhanov.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidprojecttirzhanov.data.IndicatorState
import com.example.androidprojecttirzhanov.domain.repository.UserRepository

class UserViewModelFactory(
    private val repository: UserRepository,
    private val context: Context,
    private val indicatorState: IndicatorState
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("UserViewModelFactory", "Создание ViewModel для класса: ${modelClass.simpleName}")
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repository, context, indicatorState) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}