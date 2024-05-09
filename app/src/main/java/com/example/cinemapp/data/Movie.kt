package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Movie(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("release_date") val releaseDate: Date? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("vote_average") val voteAverage: Double? = null,
)