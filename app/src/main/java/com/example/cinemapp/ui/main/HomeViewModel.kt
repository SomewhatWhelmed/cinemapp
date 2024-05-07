package com.example.cinemapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieCard
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.util.MovieMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    data class State(
        val movies: List<MovieCard> = emptyList(),
        val search: String = "",
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    fun getUpcoming() {
        viewModelScope.launch {
            _state.update { it.copy(movies = movieRepository.getUpcoming()
                ?.let { list -> MovieMapper.map(list) } ?: emptyList()) }
        }
    }
}