package com.example.cinemapp.ui.main.model

import com.google.gson.annotations.SerializedName

data class CastMember(
    val id: Int = -1,
    val name: String = "",
    val profilePath: String = "",
    val character: String = "",
)
