package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class SetFavoriteBodyDTO(
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("media_id") val mediaId: Int?,
    @SerializedName("favorite") val favorite: Boolean?
)
