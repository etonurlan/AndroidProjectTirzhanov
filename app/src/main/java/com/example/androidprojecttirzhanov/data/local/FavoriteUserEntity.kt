package com.example.androidprojecttirzhanov.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_users")
data class FavoriteUserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val username: String,
    val email: String
)