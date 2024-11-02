package com.example.androidprojecttirzhanov.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class IndicatorState {
    private val _isFilterActive = MutableStateFlow(false)
    val isFilterActive: StateFlow<Boolean> = _isFilterActive

    fun setFilterActive(active: Boolean) {
        _isFilterActive.value = active
    }
}