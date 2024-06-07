package com.example.cinemapp.ui.main

import androidx.lifecycle.ViewModel
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.data.UserPreferences

class ProfileViewModel(
    private val movieRepository: MovieRepository,
    val userPrefs: UserPreferences
) : ViewModel() {
    val session = userPrefs.getSessionId()

}