package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName

data class CastMovieCreditDTO(
    @SerializedName("id") val id: Int?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("character") val character: String?
)
