package com.example.androidprojecttirzhanov.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidprojecttirzhanov.data.FilterPreferences
import com.example.androidprojecttirzhanov.data.IndicatorState
import com.example.androidprojecttirzhanov.data.ProfilePreferences
import com.example.androidprojecttirzhanov.data.UserProfile
import com.example.androidprojecttirzhanov.data.local.FavoriteUserEntity
import com.example.androidprojecttirzhanov.domain.model.User
import com.example.androidprojecttirzhanov.domain.repository.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

class UserViewModel(
    private val repository: UserRepository,
    private val context: Context,
    private val indicatorState: IndicatorState
) : ViewModel() {
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _favoriteUsers = MutableStateFlow<List<User>>(emptyList())
    val favoriteUsers: StateFlow<List<User>> = _favoriteUsers

    private val _currentNameFilter = MutableStateFlow("")
    val currentNameFilter: StateFlow<String> = _currentNameFilter

    private val _currentOnlyEvenIds = MutableStateFlow(false)
    val currentOnlyEvenIds: StateFlow<Boolean> = _currentOnlyEvenIds

    init {
        loadFilters() // Загружаем фильтры из DataStore при инициализации
        loadFavorites()
        fetchUsers()
        checkInitialFilters()
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            ProfilePreferences.getProfile(context).collect { profile ->
                _userProfile.value = profile
            }
        }
    }

    fun saveUserProfile(profile: UserProfile) {
        viewModelScope.launch {
            ProfilePreferences.saveProfile(context, profile)
            _userProfile.value = profile
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavoriteUsers().collectLatest { favoriteEntities ->
                _favoriteUsers.value = favoriteEntities.map { entity ->
                    User(id = entity.id,
                        name = entity.name,
                        username = entity.username,
                        email = entity.email) // Конвертируем в User
                }
            }
        }
    }

    fun toggleFavoriteUser(user: User) {
        viewModelScope.launch {
            val isFavorite = _favoriteUsers.value.any { it.id == user.id }
            val favoriteEntity = FavoriteUserEntity(id = user.id,
                name = user.name,
                username = user.username,
                email = user.email)
            if (isFavorite) {
                repository.removeFavoriteUser(favoriteEntity)
            } else {
                repository.addFavoriteUser(favoriteEntity)
            }
        }
    }

    private fun loadFilters() {
        viewModelScope.launch {
            _currentNameFilter.value = FilterPreferences.getNameFilter(context).first()
            _currentOnlyEvenIds.value = FilterPreferences.getOnlyEvenIdsFilter(context).first()
        }
    }

    private fun checkInitialFilters() {
        viewModelScope.launch {
            val initialNameFilter = FilterPreferences.getNameFilter(context).first()
            val initialOnlyEvenIds = FilterPreferences.getOnlyEvenIdsFilter(context).first()

            _currentNameFilter.value = initialNameFilter
            _currentOnlyEvenIds.value = initialOnlyEvenIds

            indicatorState.setFilterActive(initialNameFilter.isNotEmpty() || initialOnlyEvenIds)
        }
    }

    fun setNameFilter(nameFilter: String) {
        _currentNameFilter.value = nameFilter
        viewModelScope.launch {
            FilterPreferences.saveNameFilter(context, nameFilter)
            indicatorState.setFilterActive(nameFilter.isNotEmpty() || _currentOnlyEvenIds.value)
            fetchUsers()
        }
    }

    fun setOnlyEvenIdsFilter(onlyEvenIds: Boolean) {
        _currentOnlyEvenIds.value = onlyEvenIds
        viewModelScope.launch {
            FilterPreferences.saveOnlyEvenIdsFilter(context, onlyEvenIds)
            indicatorState.setFilterActive(_currentNameFilter.value.isNotEmpty() || onlyEvenIds)
            fetchUsers()
        }
    }

    fun fetchUsers() {
        Log.d("UserViewModel", "Начало загрузки пользователей")
        _loading.value = true
        viewModelScope.launch {
            try {
                val allUsers = repository.getUsers()
                // Применяем фильтрацию
                val filteredUsers = allUsers.filter { user ->
                    val matchesName = currentNameFilter.value.isEmpty() || user.name.contains(currentNameFilter.value, ignoreCase = true)
                    val matchesEvenId = !currentOnlyEvenIds.value || user.id % 2 == 0
                    matchesName && matchesEvenId
                }
                _users.value = filteredUsers
                Log.d("UserViewModel", "Пользователи загружены: ${_users.value.size}")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Ошибка при загрузке пользователей: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }


}