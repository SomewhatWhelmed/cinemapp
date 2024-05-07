package com.example.cinemapp.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> Fragment.observeFlowSafely(flow: Flow<T>, callback: (T) -> Unit) {
    lifecycleScope.launch {
        flow.collect {
            callback(it)
        }
    }
}