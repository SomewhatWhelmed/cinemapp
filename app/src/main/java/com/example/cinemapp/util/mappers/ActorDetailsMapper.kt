package com.example.cinemapp.util.mappers

import com.example.cinemapp.data.model.CastMovieCreditDTO
import com.example.cinemapp.data.model.PersonDetailsDTO
import com.example.cinemapp.ui.main.model.CastMovieCredit
import com.example.cinemapp.ui.main.model.PersonDetails
import java.time.LocalDate

class ActorDetailsMapper(
    private val mediaUrlMapper: MediaUrlMapper
) {

    fun mapToDescendingYears(dates: List<String?>?): List<Int?> {
        return dates?.map { date ->
            if (date.isNullOrEmpty()) null
            else date.substring(0, 4).toInt()
        }?.distinct()?.sortedByDescending { year -> year ?: 0 } ?: emptyList()
    }

    private fun mapGenderIdToString(gender: Int): String {
        return when (gender) {
            1 -> "Female"
            2 -> "Male"
            3 -> "Non-binary"
            else -> "Not specified"
        }
    }

    fun mapToPersonDetails(
        person: PersonDetailsDTO,
        resolution: Int?
    ): PersonDetails {
        return PersonDetails(
            person.id ?: -1,
            person.biography ?: "",
            person.birthday?.let { LocalDate.parse(it) },
            person.deathday?.let { LocalDate.parse(it) },
            person.gender?.let { mapGenderIdToString(it) } ?: "",
            person.name ?: "",
            mediaUrlMapper.mapImageIdToBaseURL(person.profilePath, resolution)
        )
    }

    private fun mapToCastMovieCredit(
        credit: CastMovieCreditDTO,
        resolution: Int? = null
    ): CastMovieCredit {
        return CastMovieCredit(
            id = credit.id ?: -1,
            title = credit.title ?: "",
            posterPath = mediaUrlMapper.mapImageIdToBaseURL(credit.posterPath, resolution),
            character = credit.character ?: ""
        )
    }

    fun mapToCastMovieCreditList(
        credits: List<CastMovieCreditDTO>,
        resolution: Int? = null
    ): List<CastMovieCredit> {
        return credits.map { mapToCastMovieCredit(it, resolution) }
    }
}