package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DatePeriodDTO(
    @SerializedName("maximum") val maximum: Date? = null,
    @SerializedName("minimum") val minimum: Date? = null,
)