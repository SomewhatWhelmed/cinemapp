package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName

data class CastMemberDTO(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("profile_path") val profilePath: String? = null,
    @SerializedName("character") val character: String? = null,
)
