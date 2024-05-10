package com.example.cinemapp.data

import android.util.Log

class MovieLocalCache {

    private val upcomingCache = mutableMapOf<Int, ArrayList<Movie>>()

    fun getUpcoming(page: Int = 1): ArrayList<Movie>? {
        Log.i("CACHE", "Searching Cache...")
        return upcomingCache[page]
    }

    fun insertUpcoming(page: Int = 1, movies: ArrayList<Movie>) {
        Log.i("CACHE", "Inserting into Cache")
        upcomingCache[page] = movies
        Log.i("CACHE", page.toString())
    }
}