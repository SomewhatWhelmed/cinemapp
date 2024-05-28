package com.example.cinemapp.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.setPattern(): String{
    return DateTimeFormatter.ofPattern(DATE_PATTERN).format(this)
}

fun LocalDate?.ageAndLifespanFormat(deathday: LocalDate?): String {
    return this?.let {
        "${this.until(deathday ?: LocalDate.now()).years}" +
                " (${this.setPattern()} - ${deathday?.setPattern() ?: "present"})"
    } ?: "Not specified"
}

const val DATE_PATTERN = "dd LLLL yyyy"