package com.example.cinemapp.util.mappers

import com.example.cinemapp.data.model.MovieDTO
import com.example.cinemapp.ui.main.model.MovieCard
import java.time.LocalDate

class HomeMapper(
    private val mediaUrlMapper: MediaUrlMapper
) {
    private fun mapToCard(movieDTO: MovieDTO, imageResolution: Int? = null): MovieCard {
        return MovieCard(
            movieDTO.id ?: -1,
            mediaUrlMapper.mapImageIdToBaseURL(movieDTO.posterPath, imageResolution),
            movieDTO.releaseDate?.let { LocalDate.parse(it) },
            movieDTO.title ?: "",
            movieDTO.voteAverage ?: 0f
        )
    }

    fun mapToCardList(list: List<MovieDTO>, imageResolution: Int? = null) =
        list.map { mapToCard(it, imageResolution) }
}