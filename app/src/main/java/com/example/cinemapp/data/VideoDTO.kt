package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName

data class VideoDTO(
    @SerializedName("key") val key: String? = null,
    @SerializedName("site") val site: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("official") val official: Boolean? = null,
)
