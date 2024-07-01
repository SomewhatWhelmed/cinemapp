package com.example.cinemapp.util

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

fun setAppTheme(mode: Int, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        try {
            uiModeManager.setApplicationNightMode(mode)
        } catch (e: IllegalArgumentException) {
            Log.e("THEME", e.message ?: "Unknown error")
        }
    } else {
        AppCompatDelegate.setDefaultNightMode(mode)
    }

}

fun getSystemDefaultValue(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        UiModeManager.MODE_NIGHT_AUTO
    } else {
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}

fun getSystemLightValue(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        UiModeManager.MODE_NIGHT_NO
    } else {
        AppCompatDelegate.MODE_NIGHT_NO
    }
}

fun getSystemDarkValue(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        UiModeManager.MODE_NIGHT_YES
    } else {
        AppCompatDelegate.MODE_NIGHT_YES
    }
}