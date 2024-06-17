package com.example.cinemapp.data.model

import com.example.cinemapp.data.model.MovieDTO
import com.google.gson.annotations.SerializedName

data class MovieResponseDTO(
    @SerializedName("page") val page: Int? = null,
    @SerializedName("results") val results: ArrayList<MovieDTO> = arrayListOf(),
    @SerializedName("total_pages") val totalPages: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null
)