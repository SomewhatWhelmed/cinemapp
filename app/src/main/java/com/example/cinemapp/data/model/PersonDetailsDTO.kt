package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class PersonDetailsDTO(
    @SerializedName("id") val id: Int?,
    @SerializedName("biography") val biography: String?,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("deathday") val deathday: String?,
    @SerializedName("gender") val gender: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("place_of_birth") val placeOfBirth: String?,
    @SerializedName("profile_path") val profilePath: String?
)
