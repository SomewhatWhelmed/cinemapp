package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class ValidateWithLoginRequestDTO (
    @SerializedName("username") val username: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("request_token") val requestToken: String?
)