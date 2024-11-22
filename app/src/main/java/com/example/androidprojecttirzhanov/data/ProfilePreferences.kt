package com.example.androidprojecttirzhanov.data

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile")

object ProfilePreferences {

    private val FULL_NAME_KEY = stringPreferencesKey("full_name")
    private val AVATAR_URI_KEY = stringPreferencesKey("avatar_uri")
    private val RESUME_URL_KEY = stringPreferencesKey("resume_url")
    private val JOB_TITLE_KEY = stringPreferencesKey("job_title")
    private val FAVORITE_TIME = stringPreferencesKey("favorite_time")

    fun getProfile(context: Context): Flow<UserProfile> = context.profileDataStore.data.map { preferences ->
        UserProfile(
            fullName = preferences[FULL_NAME_KEY] ?: "",
            avatarUri = preferences[AVATAR_URI_KEY]?.let { Uri.parse(it) },
            resumeUrl = preferences[RESUME_URL_KEY] ?: "",
            jobTitle = preferences[JOB_TITLE_KEY] ?: "",
            favoriteTime = preferences[FAVORITE_TIME] ?: "",
        )
    }

    suspend fun saveProfile(context: Context, profile: UserProfile) {
        context.profileDataStore.edit { preferences ->
            preferences[FULL_NAME_KEY] = profile.fullName
            preferences[AVATAR_URI_KEY] = profile.avatarUri.toString()
            preferences[RESUME_URL_KEY] = profile.resumeUrl
            preferences[JOB_TITLE_KEY] = profile.jobTitle
            preferences[FAVORITE_TIME] = profile.favoriteTime
        }
    }
}