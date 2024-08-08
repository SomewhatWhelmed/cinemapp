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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val movieRepository: MovieRepository,
    private val profileMapper: ProfileMapper,
    private val userPreferences: UserPreferences
) : ViewModel() {

    data class State(
        val accountDetails: AccountDetails? = null,
        val isDialogOpened: Boolean = false
    ) {
        val isSignedIn: Boolean = accountDetails?.let { true } ?: false
    }

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    private val _openAccountDialog: MutableSharedFlow<AccountDetails?> = MutableSharedFlow()
    val openAccountDialog = _openAccountDialog.asSharedFlow()

    private val _signOutEvent: MutableSharedFlow<Unit> = MutableSharedFlow()
    val signOutEvent = _signOutEvent.asSharedFlow()

    fun initialSetup() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    accountDetails = getAccountDetail(),
                    isDialogOpened = false
                )
            }
        }
    }

    fun openAccountDialog() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    accountDetails = getAccountDetail(),
                    isDialogOpened = true
                )
            }
        }
    }

    fun closeAccountDialog() {
        _state.update {
            it.copy(isDialogOpened = false)
        }
    }

    private suspend fun getAccountDetail(): AccountDetails? {
        return userPreferences.getSessionId().firstOrNull()?.let { sessionId ->
            movieRepository.getAccountDetails(sessionId)?.let { details ->
                profileMapper.mapToAccountDetails(details)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            val sessionId = userPreferences.getSessionId().firstOrNull()
            sessionId?.let {
                movieRepository.deleteSession(it)
                userPreferences.deleteSessionId()
            }
            _signOutEvent.emit(Unit)
        }
    }
}