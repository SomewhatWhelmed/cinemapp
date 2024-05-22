package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName

data class CastMemberDTO(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("character") val character: String?,
)
