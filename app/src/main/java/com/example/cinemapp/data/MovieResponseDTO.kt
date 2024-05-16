package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName

data class MovieResponseDTO(
    @SerializedName("dates") var dates: DatePeriodDTO? = DatePeriodDTO(),
    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var results: ArrayList<MovieDTO> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null
)