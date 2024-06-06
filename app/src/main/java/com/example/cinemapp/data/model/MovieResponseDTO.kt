package com.example.cinemapp.data.model

import com.example.cinemapp.data.model.MovieDTO
import com.google.gson.annotations.SerializedName

data class MovieResponseDTO(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var results: ArrayList<MovieDTO> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null
)