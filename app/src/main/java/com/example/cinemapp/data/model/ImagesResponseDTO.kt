package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class ImagesResponseDTO(
    @SerializedName("backdrops") val backdrops: List<ImageDTO>?,
    @SerializedName("id") val id: Int?,
    @SerializedName("logos") val logos: List<ImageDTO>?,
    @SerializedName("posters") val posters: List<ImageDTO>?
)
