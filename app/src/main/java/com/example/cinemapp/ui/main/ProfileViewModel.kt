package com.example.cinemapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.data.UserPreferences
import com.example.cinemapp.ui.main.model.AccountDetails
import com.example.cinemapp.util.mappers.ProfileMapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val movieRepository: MovieRepository,
    private val userPrefs: UserPreferences,
    private val profileMapper: ProfileMapper
) : ViewModel() {

    data class State(
        val accountDetails: AccountDetails? = null
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state
    private val _signOut: MutableSharedFlow<Unit> = MutableSharedFlow()
    val signOut = _signOut.asSharedFlow()

    val session = userPrefs.getSessionId()


    fun signOut() {
        viewModelScope.launch {
            session.collect { sessionId ->
                sessionId?.let {
                    movieRepository.deleteSession(it)
                }
                userPrefs.deleteSessionId()
                _signOut.emit(Unit)
            }
        }
    }

    fun getAccountDetails() {
        viewModelScope.launch {
            session.collect { sessionId ->
                sessionId?.let {
                    movieRepository.getAccountDetails(it)?.let { details ->
                        _state.update {  state ->
                            state.copy(
                                accountDetails = profileMapper.mapToAccountDetails(details)
                            )
                        }
                    }
                }
            }
        }
    }



}