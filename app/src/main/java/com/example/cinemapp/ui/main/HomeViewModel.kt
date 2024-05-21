package com.example.cinemapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.ui.main.model.MovieCard
import com.example.cinemapp.util.MovieUtil
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
        val pagesLoaded: Int = 0,
    )

    private val _isPagingRunning = MutableStateFlow(false)
    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    fun getUpcoming() {
        viewModelScope.launch {
            _state.update {
                it.copy(movies = movieRepository.getUpcoming()
                    ?.let { list -> MovieUtil.map(list) } ?: emptyList())
            }
        }
    }

    fun getUpcomingNextPage() {
        if (!_isPagingRunning.value) {
            togglePagingRunning()
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        pagesLoaded = it.pagesLoaded + 1,
                        movies = it.movies.plus(movieRepository.getUpcoming(it.pagesLoaded + 1)
                            ?.let { list -> MovieUtil.map(list) } ?: emptyList()))
                }
            }
        }
    }

    fun togglePagingRunning() {
        _isPagingRunning.update {
            it.not()
        }
    }
}