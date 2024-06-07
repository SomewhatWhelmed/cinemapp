package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class RequestTokenResponseDTO (
    @SerializedName("success") val success: Boolean?,
    @SerializedName("expires_at") val expiresAt: String?,
    @SerializedName("request_token") val requestToken: String?
)