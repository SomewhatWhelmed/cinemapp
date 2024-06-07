package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class SessionResponseDTO(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("session_id") val sessionId: String?
)
