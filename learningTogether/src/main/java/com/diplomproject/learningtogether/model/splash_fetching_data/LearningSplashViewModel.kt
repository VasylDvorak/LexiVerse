package com.diplomproject.learningtogether.model.splash_fetching_data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LearningSplashViewModel: ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading= _isLoading.asStateFlow()
    init {
        viewModelScope.launch {
            delay(300)
            _isLoading.value = false
        }
    }
}