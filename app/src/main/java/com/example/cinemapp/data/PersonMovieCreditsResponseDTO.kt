package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName

data class PersonMovieCreditsResponseDTO (
    @SerializedName("id") val id: Int?,
    @SerializedName("cast") val cast: List<CastMovieCreditDTO>?
)