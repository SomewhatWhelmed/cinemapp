package com.example.cinemapp.ui.main.settings.appearance

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.UserPreferences
import com.example.cinemapp.util.getSystemDarkValue
import com.example.cinemapp.util.getSystemDefaultValue
import com.example.cinemapp.util.getSystemLightValue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsAppearanceViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _currentThemeEvent: MutableSharedFlow<ThemeMode> = MutableSharedFlow()
    val currentThemeEvent = _currentThemeEvent.asSharedFlow()

    fun getCurrentTheme() {
        viewModelScope.launch {
            _currentThemeEvent.emit(
                userPreferences.getTheme().firstOrNull()?.let {
                    when (it) {
                        getSystemDefaultValue() -> ThemeMode.SYSTEM
                        getSystemLightValue() -> ThemeMode.LIGHT
                        getSystemDarkValue() -> ThemeMode.DARK
                        else -> ThemeMode.SYSTEM
                    }
                } ?: ThemeMode.SYSTEM
            )
        }
    }

    fun setTheme(themeMode: ThemeMode) {
        viewModelScope.launch {
            userPreferences.setTheme(
                when (themeMode) {
                    ThemeMode.SYSTEM -> getSystemDefaultValue()
                    ThemeMode.LIGHT -> getSystemLightValue()
                    ThemeMode.DARK -> getSystemDarkValue()
                }
            )
        }
    }

    enum class ThemeMode {
        SYSTEM, LIGHT, DARK
    }
}