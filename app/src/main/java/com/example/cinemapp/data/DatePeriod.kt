package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DatePeriod(
    @SerializedName("maximum") val maximum: Date? = null,
    @SerializedName("minimum") val minimum: Date? = null,
)