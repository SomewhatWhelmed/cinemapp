package com.example.cinemapp.ui.main.settings.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.UserPreferences
import com.example.cinemapp.util.getDefaultLanguage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SettingsLanguageViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _currentLanguageEvent: MutableSharedFlow<String> = MutableSharedFlow()
    val currentLanguageEvent = _currentLanguageEvent.asSharedFlow()

    fun getCurrentLanguage() {
        viewModelScope.launch {
            _currentLanguageEvent.emit(
                userPreferences.getLanguage().firstOrNull() ?: getDefaultLanguage()
            )
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            userPreferences.setLanguage(language)
        }
    }

}