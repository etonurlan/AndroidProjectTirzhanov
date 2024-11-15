package com.example.androidprojecttirzhanov.domain.repository

import com.example.androidprojecttirzhanov.data.local.FavoriteUserEntity
import com.example.androidprojecttirzhanov.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(): List<User>

    fun getFavoriteUsers(): Flow<List<FavoriteUserEntity>>
    suspend fun addFavoriteUser(user: FavoriteUserEntity)
    suspend fun removeFavoriteUser(user: FavoriteUserEntity)
}