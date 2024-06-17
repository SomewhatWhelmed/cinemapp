package com.example.cinemapp.util.mappers

import com.example.cinemapp.data.model.AccountDetailsDTO
import com.example.cinemapp.data.model.MovieDTO
import com.example.cinemapp.ui.main.model.AccountDetails
import com.example.cinemapp.ui.main.model.MovieCard
import java.time.LocalDate

class ProfileMapper(
    private val urlMapper: MediaUrlMapper,
    private val mediaUrlMapper: MediaUrlMapper
) {
    fun mapToAccountDetails(
        accountDetailsDTO: AccountDetailsDTO,
        resolution: Int? = null
    ): AccountDetails {
        return AccountDetails(
            urlMapper.mapImageIdToBaseURL(accountDetailsDTO.avatar?.tmdb?.avatarPath, resolution),
            accountDetailsDTO.id ?: -1,
            accountDetailsDTO.name ?: "",
            accountDetailsDTO.username ?: ""
        )
    }
}