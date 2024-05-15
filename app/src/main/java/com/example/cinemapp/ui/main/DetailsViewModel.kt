package com.example.cinemapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.util.MovieUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    data class State(
        val details: MovieDetails = MovieDetails()
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _state.update {
                movieRepository.getMovieDetails(movieId)?.let { it1 -> State(MovieUtil.map(it1)) }
                    ?: State()
            }
        }
    }
}