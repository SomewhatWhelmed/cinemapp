package com.example.cinemapp.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.data.UserPreferences
import com.example.cinemapp.util.mappers.AuthenticationMapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val movieRepository: MovieRepository,
    private val authenticationMapper: AuthenticationMapper,
    private val userPrefs: UserPreferences
) : ViewModel() {

    private val _signInAttempt: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val signInAttempt = _signInAttempt.asSharedFlow()


    fun attemptSignIn(username: String, password: String) {
        viewModelScope.launch {
            movieRepository.getSessionId(username, password)?.let { response ->
                val sessionData = authenticationMapper.mapToSessionResponse(response)
                if (sessionData.success) {
                    userPrefs.setSessionId(sessionData.sessionId)
                }
                _signInAttempt.emit(sessionData.success)
            }
        }
    }

}