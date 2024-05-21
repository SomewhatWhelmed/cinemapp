package com.example.cinemapp.ui.main.model

import com.example.cinemapp.ui.main.model.CastMember

data class MovieCredits(
    val id: Int,
    val cast: List<CastMember> = emptyList()
)
