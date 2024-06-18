package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class StatusResponseDTO (
    @SerializedName("status_code") val statusCode: Int?,
    @SerializedName("status_message") val statusMessage: String?
)