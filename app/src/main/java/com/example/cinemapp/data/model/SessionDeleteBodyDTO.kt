package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class SessionDeleteBodyDTO(
    @SerializedName("session_id") val sessionId: String?
)
