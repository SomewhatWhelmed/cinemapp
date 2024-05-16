package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName

data class ImagesResponseDTO(
    @SerializedName("backdrops") val backdrops: List<ImageDTO>? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("logos") val logos: List<ImageDTO>? = null,
    @SerializedName("posters") val posters: List<ImageDTO>? = null
)
