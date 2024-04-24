package com.example.cinemapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import kotlinx.coroutines.launch

class SplashViewModel (
    private val movieRepository: MovieRepository
) : ViewModel(){

    fun getUpcoming() {
        viewModelScope.launch {
            movieRepository.getUpcoming()
        }
    }
}