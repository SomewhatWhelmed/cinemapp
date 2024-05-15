package com.example.cinemapp.ui.main

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val id: Int? = null,
    val backdropPath: String? = null,
    val genres: List<Genre>? = null,
    val overview: String? = null,
    val runtime: Int? = null,
    val title: String? = null
)

data class Genre(
    val id: Int? = null,
    val name: String? = null
)
