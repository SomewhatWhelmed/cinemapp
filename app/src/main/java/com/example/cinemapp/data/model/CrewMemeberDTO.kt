package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class CrewMemberDTO(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("department") val department: String?,
    @SerializedName("job") val job: String?,
)
