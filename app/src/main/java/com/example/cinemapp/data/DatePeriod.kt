package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DatePeriod (
    @SerializedName("maximum"      ) var maximum      : Date?        = null,
    @SerializedName("minimum"      ) var minimum      : Date?        = null,
)