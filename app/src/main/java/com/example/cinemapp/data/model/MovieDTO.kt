package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class MovieDTO(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("vote_average") val voteAverage: Float? = null,
    @SerializedName("rating") val rating: Int? = null
)