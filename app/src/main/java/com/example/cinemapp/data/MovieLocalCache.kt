package com.example.cinemapp.data

import android.util.Log

class MovieLocalCache {

    private val upcomingCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private val popularCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private val topRatedCache = mutableMapOf<Int, ArrayList<MovieDTO>>()


    fun get(listType: MovieRepository.ListType, page: Int = 1): ArrayList<MovieDTO>? {
        Log.i("CACHE", "Searching Cache...")
        return when(listType) {
            MovieRepository.ListType.UPCOMING -> upcomingCache[page]
            MovieRepository.ListType.POPULAR -> popularCache[page]
            MovieRepository.ListType.TOP_RATED -> topRatedCache[page]
        }
    }

    fun insert(listType: MovieRepository.ListType, page: Int = 1, movies: ArrayList<MovieDTO>) {
        Log.i("CACHE", "Inserting into Cache")
        when(listType) {
            MovieRepository.ListType.UPCOMING -> upcomingCache[page] = movies
            MovieRepository.ListType.POPULAR -> popularCache[page] = movies
            MovieRepository.ListType.TOP_RATED -> topRatedCache[page] = movies
        }
    }
}