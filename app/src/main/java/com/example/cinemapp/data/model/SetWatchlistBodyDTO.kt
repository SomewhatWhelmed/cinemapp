package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class SetWatchlistBodyDTO(
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("media_id") val mediaId: Int?,
    @SerializedName("watchlist") val watchlist: Boolean?
)
