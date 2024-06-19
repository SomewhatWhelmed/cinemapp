package com.example.cinemapp.ui.main.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.data.UserPreferences
import com.example.cinemapp.ui.main.model.AccountDetails
import com.example.cinemapp.ui.main.model.MovieCard
import com.example.cinemapp.ui.main.model.MovieListInfo
import com.example.cinemapp.util.mappers.MovieListMapper
import com.example.cinemapp.util.mappers.ProfileMapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val movieRepository: MovieRepository,
    private val userPrefs: UserPreferences,
    private val profileMapper: ProfileMapper,
    private val movieListMapper: MovieListMapper
) : ViewModel() {

    data class State(
        val accountDetails: AccountDetails? = null,
        val isLoading: Boolean = true,
        val pagesLoaded: Int = 0,
        val totalPages: Int = 1,
        val movies: List<MovieCard> = emptyList(),
        val movieListType: MovieRepository.MovieListType = MovieRepository.MovieListType.FAVORITE
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    private val _signOut: MutableSharedFlow<Unit> = MutableSharedFlow()
    val signOut = _signOut.asSharedFlow()

    private val _notSignedIn: MutableSharedFlow<Unit> = MutableSharedFlow()
    val notSignedIn = _notSignedIn.asSharedFlow()

    private var isPaging = false


    fun setupLoading() {
        _state.update { it.copy(isLoading = true) }
    }

    fun signOut() {
        viewModelScope.launch {
            val sessionId = userPrefs.getSessionId().firstOrNull()
            sessionId?.let {
                movieRepository.deleteSession(it)
                userPrefs.deleteSessionId()
            }
            _signOut.emit(Unit)
        }
    }

    fun getInitialData() {
        getAccountDetails()
        if (state.value.pagesLoaded == 0)
            getFavoriteNextPage()
        else {
            getNextPage(state.value.movieListType, true)
        }
    }

    private fun getAccountDetails() {
        viewModelScope.launch {
            val sessionId = userPrefs.getSessionId().firstOrNull()
            sessionId?.let {
                movieRepository.getAccountDetails(it)?.let { details ->
                    _state.update { state ->
                        state.copy(
                            accountDetails = profileMapper.mapToAccountDetails(details)
                        )
                    }
                }
            } ?: _notSignedIn.emit(Unit)

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

    fun getNextPage(movieListType: MovieRepository.MovieListType = state.value.movieListType, resetPaging: Boolean = false) {
        if (!isPaging) {
            setPagingRunning(true)
            viewModelScope.launch {
                val sessionId = userPrefs.getSessionId().firstOrNull()
                sessionId?.let {
                    _state.update {
                        val newPage = if (resetPaging) 1 else if (movieListType == it.movieListType) it.pagesLoaded + 1 else 1
                        val listInfo = movieRepository.getMovieList(
                            movieListType,
                            newPage,
                            sessionId = sessionId
                        )?.let { listInfo -> movieListMapper.mapToCardListInfo(listInfo, 400) }
                            ?: MovieListInfo()
                        it.copy(
                            pagesLoaded = newPage,
                            movieListType = movieListType,
                            movies = listInfo.results,
                            totalPages = listInfo.totalPages
                        )
                    }
                } ?: setPagingRunning(false)
            }
        }
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