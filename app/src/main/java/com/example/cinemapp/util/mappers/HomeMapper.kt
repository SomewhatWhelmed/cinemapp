package com.example.cinemapp.util.mappers

import com.example.cinemapp.data.MovieDTO
import com.example.cinemapp.ui.main.model.MovieCard
import java.time.LocalDate

class HomeMapper(
    private val urlMapper: URLMapper
) {
    private fun mapMovieDTOToCard(movieDTO: MovieDTO, imageResolution: Int? = null): MovieCard {
        return MovieCard(
            movieDTO.id ?: -1,
            urlMapper.mapImageIdToBaseURL(movieDTO.posterPath, imageResolution),
            movieDTO.releaseDate?.let { LocalDate.parse(it) },
            movieDTO.title ?: "",
            movieDTO.voteAverage ?: 0f
        )
    }

    fun mapMovieDTOListToCardList(list: List<MovieDTO>, imageResolution: Int? = null) =
        list.map { mapMovieDTOToCard(it, imageResolution) }
}