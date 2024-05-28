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
        val listType: MovieRepository.ListType = MovieRepository.ListType.UPCOMING
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state
    private var isPaging = false


    fun getUpcomingNextPage() {
        getNextPage(MovieRepository.ListType.UPCOMING)
    }

    fun getPopularNextPage() {
        getNextPage(MovieRepository.ListType.POPULAR)
    }

    fun getTopRatedNextPage() {
        getNextPage(MovieRepository.ListType.TOP_RATED)
    }

    fun getNextPage(listType: MovieRepository.ListType = state.value.listType) {
        if (!isPaging) {
            setPagingRunning(true)
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        pagesLoaded = if (listType == it.listType) it.pagesLoaded + 1 else 1,
                        listType = listType,
                        movies = (if (listType == it.listType) it.movies else emptyList()).plus(
                            movieRepository.getList(
                                listType,
                                if (listType == it.listType) it.pagesLoaded + 1 else 1
                            )
                                ?.let { list -> MovieUtil.mapListMovie(list) } ?: emptyList()))
                }
            }
        }
    }

    fun setPagingRunning(isRunning: Boolean) {
        isPaging = isRunning
    }
}