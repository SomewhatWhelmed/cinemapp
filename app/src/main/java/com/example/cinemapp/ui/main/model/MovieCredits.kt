package com.example.cinemapp.ui.main.model

import com.example.cinemapp.ui.main.model.CastMember

data class MovieCredits(
    val id: Int = -1,
    val cast: List<CastMember> = emptyList(),
    val crew: List<CrewMember> = emptyList()
)
