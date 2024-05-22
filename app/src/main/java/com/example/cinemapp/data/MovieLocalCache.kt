package com.example.cinemapp.data

import android.util.Log

class MovieLocalCache {

    private val upcomingCache = mutableMapOf<Int, ArrayList<MovieDTO>>()

    fun getUpcoming(page: Int = 1): ArrayList<MovieDTO>? {
        Log.i("CACHE", "Searching Cache...")
        return upcomingCache[page]
    }

    fun insertUpcoming(page: Int = 1, movies: ArrayList<MovieDTO>) {
        Log.i("CACHE", "Inserting into Cache")
        upcomingCache[page] = movies
    }
}