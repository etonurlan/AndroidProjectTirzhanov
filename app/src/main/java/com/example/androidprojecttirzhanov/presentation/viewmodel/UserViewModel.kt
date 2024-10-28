package com.example.androidprojecttirzhanov.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidprojecttirzhanov.domain.model.User
import com.example.androidprojecttirzhanov.domain.repository.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        // Отладочное сообщение при создании ViewModel
        Log.d("UserViewModel", "ViewModel создан")
        fetchUsers()
    }

    fun fetchUsers() {
        Log.d("UserViewModel", "Начало загрузки пользователей")
        _loading.value = true
        viewModelScope.launch {
            try {
                _users.value = repository.getUsers()
                Log.d("UserViewModel", "Пользователи загружены: ${_users.value.size}")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Ошибка при загрузке пользователей: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
}