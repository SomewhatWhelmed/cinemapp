package com.example.cinemapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.Movie
import com.example.cinemapp.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    data class State(
        val movies: List<Movie> = emptyList(),
        val search: String = "",
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    fun getUpcoming() {
        viewModelScope.launch {
            _state.update { it.copy(movies = movieRepository.getUpcoming() ?: emptyList()) }
        }
    }
}