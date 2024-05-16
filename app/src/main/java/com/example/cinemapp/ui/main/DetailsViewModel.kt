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
        val details: MovieDetails? = null,
        val cast: List<CastMember> = emptyList()
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _state.update { it ->
                it.copy(
                    details = movieRepository.getMovieDetails(movieId)?.let { details ->
                        MovieUtil.map(details, 500)
                    },
                    cast = movieRepository.getCredits(movieId)
                        ?.let { MovieUtil.map(it, 500).cast }
                        ?: emptyList()
                )
            }
        }
    }
}