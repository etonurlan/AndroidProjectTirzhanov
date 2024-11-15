package com.example.androidprojecttirzhanov.data

import android.net.Uri

data class UserProfile(
    val fullName: String = "",
    val avatarUri: Uri? = null,
    val resumeUrl: String = "",
    val jobTitle: String = "",
)