package com.example.androidprojecttirzhanov.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "filter_preferences")

object FilterPreferences {
    private val NAME_FILTER_KEY = stringPreferencesKey("name_filter")
    private val ONLY_EVEN_IDS_KEY = booleanPreferencesKey("only_even_ids")

    fun getNameFilter(context: Context): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[NAME_FILTER_KEY] ?: ""
        }
    }

    fun getOnlyEvenIdsFilter(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[ONLY_EVEN_IDS_KEY] ?: false
        }
    }

    suspend fun saveNameFilter(context: Context, nameFilter: String) {
        context.dataStore.edit { preferences ->
            preferences[NAME_FILTER_KEY] = nameFilter
        }
    }

    suspend fun saveOnlyEvenIdsFilter(context: Context, onlyEvenIds: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONLY_EVEN_IDS_KEY] = onlyEvenIds
        }
    }
}