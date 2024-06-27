package com.example.cinemapp.ui.splash

import android.app.UiModeManager
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.data.UserPreferences
import com.example.cinemapp.util.getSystemDefaultValue
import com.example.cinemapp.util.setAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SplashViewModel(
    private val movieRepository: MovieRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    private val _goToNextScreen: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val goToNextScreen = _goToNextScreen.asSharedFlow()

    private val session = userPrefs.getSessionId()

    fun handleInitialData() {
        viewModelScope.launch {
            movieRepository.getMovieList(MovieRepository.MovieListType.UPCOMING)
            val validSession = session.firstOrNull()
                ?.let { movieRepository.getAccountDetails(it)?.let { true } ?: false } ?: false
            if (!validSession) userPrefs.deleteSessionId()

            delay(2000)
            _goToNextScreen.emit(validSession)
        }
    }

    fun setTheme() {
        viewModelScope.launch {
            userPrefs.setTheme(
                userPrefs.getTheme().firstOrNull() ?: getSystemDefaultValue()
            )
        }
    }
}