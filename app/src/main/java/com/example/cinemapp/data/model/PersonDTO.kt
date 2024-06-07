package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class PersonDTO(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("profile_path") val profilePath: String?
)
