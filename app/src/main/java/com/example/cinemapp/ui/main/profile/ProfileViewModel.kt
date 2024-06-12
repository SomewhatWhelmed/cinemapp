package com.example.cinemapp.ui.main.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.data.UserPreferences
import com.example.cinemapp.ui.main.model.AccountDetails
import com.example.cinemapp.ui.main.model.MovieCard
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
    private val profileMapper: ProfileMapper
) : ViewModel() {

    data class State(
        val accountDetails: AccountDetails? = null,
        val pagesLoaded: Int = 0,
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
        getFavoriteNextPage()
    }

    private fun getAccountDetails() {
        Log.i("TEST", "Entered getAccountDetails")
        viewModelScope.launch {
            Log.i("TEST", "Entered viewModelScope")
            val sessionId = userPrefs.getSessionId().firstOrNull()
            Log.i("TEST", "Entered session.collect")
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

    fun getFavoriteNextPage() {
        getNextPage(MovieRepository.MovieListType.FAVORITE)
    }

    fun getWatchlistNextPage() {
        getNextPage(MovieRepository.MovieListType.WATCHLIST)
    }

    fun getRatedNextPage() {
        getNextPage(MovieRepository.MovieListType.RATED)
    }

    fun getNextPage(movieListType: MovieRepository.MovieListType = state.value.movieListType) {
        if (!isPaging) {
            setPagingRunning(true)
            viewModelScope.launch {
                val sessionId = userPrefs.getSessionId().firstOrNull()
                _state.update {
                    it.copy(
                        pagesLoaded = if (movieListType == it.movieListType) it.pagesLoaded + 1 else 1,
                        movieListType = movieListType,
                        movies = (if (movieListType == it.movieListType) it.movies else emptyList()).plus(
                            movieRepository.getMovieList(
                                movieListType,
                                if (movieListType == it.movieListType) it.pagesLoaded + 1 else 1,
                                sessionId = sessionId
                            )
                                ?.let { list -> profileMapper.mapToCardList(list, 400) }
                                ?: emptyList()))
                }
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

}