package com.example.cinemapp.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.ui.main.model.MovieCard
import com.example.cinemapp.util.mappers.MovieListMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepository,
    private val movieListMapper: MovieListMapper
) : ViewModel() {

    data class State(
        val movies: List<MovieCard> = emptyList(),
        val pagesLoaded: Int = 0,
        val movieListType: MovieRepository.MovieListType = MovieRepository.MovieListType.UPCOMING
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state
    private var isPaging = false


    fun getUpcomingNextPage() {
        getNextPage(MovieRepository.MovieListType.UPCOMING)
    }

    fun getPopularNextPage() {
        getNextPage(MovieRepository.MovieListType.POPULAR)
    }

    fun getTopRatedNextPage() {
        getNextPage(MovieRepository.MovieListType.TOP_RATED)
    }

    fun getNextPage(movieListType: MovieRepository.MovieListType = state.value.movieListType) {
        if (!isPaging) {
            setPagingRunning(true)
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        pagesLoaded = if (movieListType == it.movieListType) it.pagesLoaded + 1 else 1,
                        movieListType = movieListType,
                        movies = (if (movieListType == it.movieListType) it.movies else emptyList()).plus(
                            movieRepository.getMovieList(
                                movieListType,
                                if (movieListType == it.movieListType) it.pagesLoaded + 1 else 1
                            )
                                ?.let { list -> movieListMapper.mapToCardList(list.results, 400) }
                                ?: emptyList()))
                }
            }
        }
    }

    fun setPagingRunning(isRunning: Boolean) {
        isPaging = isRunning
    }
}