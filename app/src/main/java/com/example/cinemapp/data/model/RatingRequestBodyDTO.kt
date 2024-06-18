package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class RatingRequestBodyDTO(
    @SerializedName("value") val value: Float?
)
