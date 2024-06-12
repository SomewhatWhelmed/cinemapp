package com.example.cinemapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.data.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val movieRepository: MovieRepository,
    userPrefs: UserPreferences
) : ViewModel() {

    private val _gotoMainScreen: MutableSharedFlow<Unit> = MutableSharedFlow()
    val gotoMainScreen = _gotoMainScreen.asSharedFlow()

    val session = userPrefs.getSessionId()

    fun handleInitialData() {
        viewModelScope.launch {
            movieRepository.getMovieList(MovieRepository.MovieListType.UPCOMING)
            delay(2000)
            _gotoMainScreen.emit(Unit)
        }
    }
}