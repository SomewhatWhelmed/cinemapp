package com.example.cinemapp.util

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safeNavigateWithArgs(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run {
        navigate(direction)
    }
}