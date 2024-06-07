package com.example.cinemapp.data.model

import com.example.cinemapp.data.model.CastMovieCreditDTO
import com.google.gson.annotations.SerializedName

data class PersonMovieCreditsResponseDTO (
    @SerializedName("id") val id: Int?,
    @SerializedName("cast") val cast: List<CastMovieCreditDTO>?
)