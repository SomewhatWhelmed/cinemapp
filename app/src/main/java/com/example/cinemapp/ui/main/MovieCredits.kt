package com.example.cinemapp.ui.main

data class MovieCredits(
    val id: Int,
    val cast: List<CastMember> = emptyList()
)
