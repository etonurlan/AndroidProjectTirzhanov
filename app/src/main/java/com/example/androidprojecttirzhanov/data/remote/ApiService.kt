package com.example.androidprojecttirzhanov.data.remote

import android.content.Context
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.androidprojecttirzhanov.data.model.User
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient


interface ApiService {
    @GET("users")
    suspend fun getUsers(@Query("limit") limit: Int = 10): Response<List<User>>
}

object ApiServiceBuilder {
//    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
//
//    fun create(): ApiService {
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ApiService::class.java)
//    }
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    fun create(context: Context): ApiService {
        // Создаем OkHttpClient с ChuckerInterceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(context)) // Добавляем ChuckerInterceptor
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Используем клиента с ChuckerInterceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}