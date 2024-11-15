package com.example.androidprojecttirzhanov.data.repository

import android.util.Log
import com.example.androidprojecttirzhanov.data.local.FavoriteUserDao
import com.example.androidprojecttirzhanov.data.local.FavoriteUserEntity
import com.example.androidprojecttirzhanov.data.remote.ApiService
import com.example.androidprojecttirzhanov.domain.model.User
import com.example.androidprojecttirzhanov.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val favoriteUserDao: FavoriteUserDao
) : UserRepository {
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

    override fun getFavoriteUsers(): Flow<List<FavoriteUserEntity>> {
        return favoriteUserDao.getFavoriteUsers().map { entities ->
            entities.map { entity ->
                FavoriteUserEntity(id = entity.id, name = entity.name, username = "", email = "")
            }
        }
    }
    override suspend fun addFavoriteUser(user: FavoriteUserEntity) = withContext(Dispatchers.IO) {
        favoriteUserDao.addFavoriteUser(user)
    }

    override suspend fun removeFavoriteUser(user: FavoriteUserEntity) = withContext(Dispatchers.IO) {
        favoriteUserDao.removeFavoriteUser(user)
    }
}