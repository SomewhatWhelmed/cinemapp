package com.example.cinemapp.ui.main.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.util.Date

data class MovieCard (
    val id: Int = -1,
    val posterPath: String = "",
    val releaseDate: LocalDate? = null,
    val title: String = "",
    val voteAverage: Float = 0.0f,
)