package com.example.cinemapp.ui.main.model

data class SearchCard(
    val id: Int = -1,
    val image: String = "",
    val text: String = "",
    val type: SearchType
)

enum class SearchType {
    MOVIE, ACTOR
}
