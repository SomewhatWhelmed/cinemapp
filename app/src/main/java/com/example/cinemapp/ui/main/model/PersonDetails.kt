package com.example.cinemapp.ui.main.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.util.Date

data class PersonDetails(
    val id: Int = -1,
    val biography: String = "",
    val birthday: LocalDate? = null,
    val deathday: LocalDate? = null,
    val gender: String = "",
    val name: String = "",
    val profilePath: String = ""
)
