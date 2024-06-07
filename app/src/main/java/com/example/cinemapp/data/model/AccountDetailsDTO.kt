package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class AccountDetailsDTO(
    @SerializedName("avatar") val avatar: AccountAvatarDTO?,
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("username") val username: String?
)
