package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class SearchPersonResponseDTO(
    @SerializedName("page") val page: Int?,
    @SerializedName("results") val results: List<PersonDTO>?,
    @SerializedName("total_pages") val totalPages: Int?,
    @SerializedName("total_results") val totalResults: Int?
)
