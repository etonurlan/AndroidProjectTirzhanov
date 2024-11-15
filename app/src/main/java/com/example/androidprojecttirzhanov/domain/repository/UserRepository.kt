package com.example.androidprojecttirzhanov.domain.repository

import com.example.androidprojecttirzhanov.domain.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
}