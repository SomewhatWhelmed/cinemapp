package com.example.cinemapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.data.UserPreferences
import com.example.cinemapp.ui.main.model.AccountDetails
import com.example.cinemapp.util.mappers.ProfileMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val movieRepository: MovieRepository,
    private val profileMapper: ProfileMapper,
    private val userPreferences: UserPreferences
) : ViewModel() {

    data class State(
        val accountDetails: AccountDetails? = null
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    fun getAccountDetail() {
        viewModelScope.launch {
            userPreferences.getSessionId().firstOrNull()?.let { sessionId ->
                movieRepository.getAccountDetails(sessionId)?.let { details ->
                    _state.update {
                        it.copy(
                            accountDetails = profileMapper.mapToAccountDetails(details)
                        )
                    }
                }
            }
        }
    }

}