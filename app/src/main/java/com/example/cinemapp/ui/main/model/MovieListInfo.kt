package com.example.cinemapp.ui.main.model

data class MovieListInfo(
    val page: Int = 1,
    val results: List<MovieCard> = emptyList(),
    val totalPages: Int = 1,
    val totalResults: Int = 0
)
