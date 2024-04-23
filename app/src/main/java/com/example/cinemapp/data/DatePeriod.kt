package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName

data class DatePeriod (
    @SerializedName("maximum"      ) var maximum      : String?        = null,
    @SerializedName("minimum"      ) var minimum      : String?        = null,
)