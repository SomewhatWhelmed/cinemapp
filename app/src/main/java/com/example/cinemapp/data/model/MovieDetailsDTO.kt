package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class MovieDetailsDTO(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("genres") val genres: List<GenreDTO>? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("runtime") val runtime: Int? = null,
    @SerializedName("title") val title: String? = null
)
