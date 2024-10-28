package com.example.androidprojecttirzhanov.data.repository

import android.util.Log
import com.example.androidprojecttirzhanov.data.remote.ApiService
import com.example.androidprojecttirzhanov.domain.model.User
import com.example.androidprojecttirzhanov.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(private val apiService: ApiService) : UserRepository {
    override suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
        Log.d("UserRepositoryImpl", "Запрос пользователей к API")
        val response = apiService.getUsers()
        Log.d("UserRepositoryImpl", "Получен ответ от API: ${response.code()}")
        if (response.isSuccessful) {
            Log.d("UserRepositoryImpl", "Пользователи успешно получены: ${response.body()?.size}")
            response.body()?.map {
                User(it.id, it.name, it.username, it.email)
            } ?: emptyList()
        } else {
            Log.e("UserRepositoryImpl", "Error fetching users: ${response.code()}")
            emptyList()
        }
    }
}