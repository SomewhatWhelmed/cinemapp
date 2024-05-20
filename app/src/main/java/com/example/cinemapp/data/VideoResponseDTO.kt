package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName

data class VideoResponseDTO(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("results") val results: List<VideoDTO>? = null,
)
