package com.example.cinemapp.util.mappers

import com.example.cinemapp.data.model.MovieDTO
import com.example.cinemapp.data.model.PersonDTO
import com.example.cinemapp.ui.main.model.SearchCard
import com.example.cinemapp.ui.main.model.SearchType

class SearchMapper(
    private val mediaUrlMapper: MediaUrlMapper
) {
    private fun mapToMovieCard(movieDTO: MovieDTO, imageResolution: Int? = null): SearchCard {
        return SearchCard(
            movieDTO.id ?: -1,
            mediaUrlMapper.mapImageIdToBaseURL(movieDTO.posterPath, imageResolution),
            movieDTO.title ?: "",
            SearchType.MOVIE
        )
    }

    private fun mapToPersonCard(personDTO: PersonDTO, imageResolution: Int? = null): SearchCard {
        return SearchCard(
            personDTO.id ?: -1,
            mediaUrlMapper.mapImageIdToBaseURL(personDTO.profilePath, imageResolution),
            personDTO.name ?: "",
            SearchType.ACTOR
        )
    }

    fun mapToMovieCardList(list: List<MovieDTO>, imageResolution: Int? = null) =
        list.map { mapToMovieCard(it, imageResolution) }

    fun mapToPersonCardList(list: List<PersonDTO>, imageResolution: Int? = null) =
        list.map { mapToPersonCard(it, imageResolution) }
}