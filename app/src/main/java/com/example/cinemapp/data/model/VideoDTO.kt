package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class VideoDTO(
    @SerializedName("key") val key: String?,
    @SerializedName("site") val site: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("official") val official: Boolean?,
)
