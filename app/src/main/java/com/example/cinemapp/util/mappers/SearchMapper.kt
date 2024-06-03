package com.example.cinemapp.util.mappers

import com.example.cinemapp.data.MovieDTO
import com.example.cinemapp.data.PersonDTO
import com.example.cinemapp.ui.main.model.SearchCard
import com.example.cinemapp.ui.main.model.SearchType

class SearchMapper(
    private val urlMapper: URLMapper
) {
    private fun mapMovieDTOToCard(movieDTO: MovieDTO, imageResolution: Int? = null): SearchCard {
        return SearchCard(
            movieDTO.id ?: -1,
            urlMapper.mapImageIdToBaseURL(movieDTO.posterPath, imageResolution),
            movieDTO.title ?: "",
            SearchType.MOVIE
        )
    }

    private fun mapPersonDTOToCard(personDTO: PersonDTO, imageResolution: Int? = null): SearchCard {
        return SearchCard(
            personDTO.id ?: -1,
            urlMapper.mapImageIdToBaseURL(personDTO.profilePath, imageResolution),
            personDTO.name ?: "",
            SearchType.ACTOR
        )
    }

    fun mapMovieDTOListToCardList(list: List<MovieDTO>, imageResolution: Int? = null) =
        list.map { mapMovieDTOToCard(it, imageResolution) }

    fun mapPersonDTOListToCardList(list: List<PersonDTO>, imageResolution: Int? = null) =
        list.map { mapPersonDTOToCard(it, imageResolution) }
}