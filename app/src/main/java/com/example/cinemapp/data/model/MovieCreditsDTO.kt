package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class MovieCreditsDTO(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("cast") val cast: List<CastMemberDTO>? = null
)
