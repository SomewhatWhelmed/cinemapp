package com.example.cinemapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.Movie
import com.example.cinemapp.data.MovieRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movieList = MutableStateFlow<List<Movie>>(emptyList())
    val movieList : StateFlow<List<Movie>> = _movieList

    init {
        getUpcoming()
    }

    private fun getUpcoming() {
        viewModelScope.launch {
            _movieList.value = movieRepository.getUpcoming() ?: emptyList()
        }
    }
}