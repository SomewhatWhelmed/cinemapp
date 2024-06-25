package com.example.cinemapp.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.data.UserPreferences
import com.example.cinemapp.data.model.MovieDetailsDTO
import com.example.cinemapp.ui.main.model.AccountDetails
import com.example.cinemapp.ui.main.model.MovieCard
import com.example.cinemapp.ui.main.model.MovieListInfo
import com.example.cinemapp.util.mappers.MovieListMapper
import com.example.cinemapp.util.mappers.ProfileMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val movieRepository: MovieRepository,
    private val userPrefs: UserPreferences,
    private val profileMapper: ProfileMapper,
    private val movieListMapper: MovieListMapper
) : ViewModel() {

    data class State(
        val isLoading: Boolean = true,
        val pagesLoaded: Int = 0,
        val totalPages: Int = 1,
        val movies: List<MovieCard> = emptyList(),
        val movieListType: MovieRepository.MovieListType = MovieRepository.MovieListType.FAVORITE
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    private var isPaging = false

    fun getCurrentListType() = state.value.movieListType

    fun setupLoading() {
        _state.update { it.copy(isLoading = true) }
    }

    fun getInitialData() {
        viewModelScope.launch {
            val sessionId = userPrefs.getSessionId().firstOrNull()
            sessionId?.let {
                val pagingDetails = getNextPageData(
                    if (state.value.pagesLoaded == 0) MovieRepository.MovieListType.FAVORITE else state.value.movieListType,
                    sessionId = sessionId,
                    resetPaging = true
                )
                _state.update {
                    pagingDetails
                }
            } ?: run {
                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun getFavoriteNextPage() {
        getNextPage(MovieRepository.MovieListType.FAVORITE)
    }

    private fun getWatchlistNextPage() {
        getNextPage(MovieRepository.MovieListType.WATCHLIST)
    }

    private fun getRatedNextPage() {
        getNextPage(MovieRepository.MovieListType.RATED)
    }

    fun getNextPage(
        movieListType: MovieRepository.MovieListType = state.value.movieListType,
        resetPaging: Boolean = false
    ) {
        viewModelScope.launch {
            val sessionId = userPrefs.getSessionId().firstOrNull()
            sessionId?.let {
                if (!isPaging) {
                    setPagingRunning(true)
                    _state.update {
                        getNextPageData(movieListType, resetPaging, sessionId)
                    }
                }
            }
        }
    }


    private suspend fun getNextPageData(
        movieListType: MovieRepository.MovieListType = state.value.movieListType,
        resetPaging: Boolean = false,
        sessionId: String
    ): State {
        val newPage =
            if (resetPaging) 1
            else if (movieListType == state.value.movieListType) state.value.pagesLoaded + 1
            else 1
        val listInfo = movieRepository.getMovieList(
            movieListType,
            newPage,
            sessionId = sessionId
        )?.let { listInfo -> movieListMapper.mapToCardListInfo(listInfo, 400) }
            ?: MovieListInfo()
        val newValue = state.value.copy(
            pagesLoaded = newPage,
            movieListType = movieListType,
            movies = listInfo.results,
            totalPages = listInfo.totalPages,
            isLoading = false
        )
        if (newValue == state.value) setPagingRunning(false)
        return newValue
    }


    fun setPagingRunning(isRunning: Boolean) {
        isPaging = isRunning
    }

    fun loadPage(tabPosition: Int) {
        when (tabPosition) {
            ProfileFragmentTabs.FAVORITES.ordinal -> getFavoriteNextPage()
            ProfileFragmentTabs.WATCHLIST.ordinal -> getWatchlistNextPage()
            ProfileFragmentTabs.RATED.ordinal -> getRatedNextPage()
        }
    }

    fun isLastPage(): Boolean {
        return state.value.pagesLoaded >= state.value.totalPages
    }
}