package com.example.cinemapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _gotoMainScreen: MutableSharedFlow<Unit> = MutableSharedFlow()
    val gotoMainScreen = _gotoMainScreen.asSharedFlow()


    fun handleInitialData() {
        viewModelScope.launch {
            movieRepository.getList(MovieRepository.ListType.UPCOMING)
            delay(2000)
            _gotoMainScreen.emit(Unit)
        }
    }
}