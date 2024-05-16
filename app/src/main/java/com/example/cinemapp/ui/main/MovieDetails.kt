package com.example.cinemapp.ui.main

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val id: Int,
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
