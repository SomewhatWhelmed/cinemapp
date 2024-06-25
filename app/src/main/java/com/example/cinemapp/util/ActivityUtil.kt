package com.example.cinemapp.util

import android.app.Activity
import android.app.AppComponentFactory
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun Activity.finishThenStart(context: Context?, cls: Class<*>) {
    val intent = Intent(context, cls)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
    finish()
}

fun <T>AppCompatActivity.observeFlowSafely(flow: Flow<T>?, callback: (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow?.collect {
                callback(it)
            }
        }
    }
}