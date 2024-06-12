package com.example.cinemapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent

fun Activity.finishThenStart(context: Context?, cls: Class<*>) {
    val intent = Intent(context, cls)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
    finish()
}