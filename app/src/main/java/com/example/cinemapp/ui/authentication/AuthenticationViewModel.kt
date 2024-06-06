package com.example.cinemapp.ui.authentication

import androidx.lifecycle.ViewModel
import com.example.cinemapp.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthenticationViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    data class State(
        val username: String? = null
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state



}