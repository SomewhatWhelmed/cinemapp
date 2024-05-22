package com.example.cinemapp.ui.main.model

data class MovieDetails(
    val id: Int = -1,
    val backdropPath: String = "",
    val genres: List<Genre> = emptyList(),
    val overview: String = "",
    val runtime: Int = 0,
    val title: String = ""
)

data class Genre(
    val id: Int = -1,
    val name: String = ""
)
