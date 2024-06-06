package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class SessionRequestDTO(
    @SerializedName("request_token") val requestToken: String?
)
