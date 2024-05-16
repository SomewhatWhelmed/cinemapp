package com.example.cinemapp.ui.main

import com.google.gson.annotations.SerializedName

data class CastMember(
    val id: Int,
    val name: String = "",
    val profilePath: String = "",
    val character: String = "",
)
