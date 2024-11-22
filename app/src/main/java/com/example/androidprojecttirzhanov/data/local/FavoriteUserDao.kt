package com.example.androidprojecttirzhanov.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM favorite_users")
    fun getFavoriteUsers(): Flow<List<FavoriteUserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavoriteUser(user: FavoriteUserEntity)

    @Delete
    fun removeFavoriteUser(user: FavoriteUserEntity)
}