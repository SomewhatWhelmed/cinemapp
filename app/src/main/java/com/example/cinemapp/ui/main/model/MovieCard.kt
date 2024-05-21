package com.example.cinemapp.ui.main.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class MovieCard (
    val id: Int? = null,
    val posterPath: String? = null,
    val releaseDate: Date? = null,
    val title: String? = null,
    val voteAverage: Double? = null,
)