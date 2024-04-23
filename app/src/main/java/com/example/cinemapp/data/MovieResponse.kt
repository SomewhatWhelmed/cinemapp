package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class MovieResponse (
    @SerializedName("dates"         ) var dates        : DatePeriod?        = DatePeriod(),
    @SerializedName("page"          ) var page         : Int?               = null,
    @SerializedName("results"       ) var results      : ArrayList<Movie>   = arrayListOf(),
    @SerializedName("total_pages"   ) var totalPages   : Int?               = null,
    @SerializedName("total_results" ) var totalResults : Int?               = null
)