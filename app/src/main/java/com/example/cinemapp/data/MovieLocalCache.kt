package com.example.cinemapp.data

import android.util.Log

class MovieLocalCache {

    private val upcomingCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private val popularCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private val topRatedCache = mutableMapOf<Int, ArrayList<MovieDTO>>()

    private var personCredits: PersonMovieCreditsResponseDTO? = null

    fun getMovieList(listType: MovieRepository.ListType, page: Int = 1): ArrayList<MovieDTO>? {
        Log.i("CACHE", "Searching Cache...")
        return when (listType) {
            MovieRepository.ListType.UPCOMING -> upcomingCache[page]
            MovieRepository.ListType.POPULAR -> popularCache[page]
            MovieRepository.ListType.TOP_RATED -> topRatedCache[page]
        }
    }

    fun insert(listType: MovieRepository.ListType, page: Int = 1, movies: ArrayList<MovieDTO>) {
        Log.i("CACHE", "Inserting into Cache")
        when (listType) {
            MovieRepository.ListType.UPCOMING -> upcomingCache[page] = movies
            MovieRepository.ListType.POPULAR -> popularCache[page] = movies
            MovieRepository.ListType.TOP_RATED -> topRatedCache[page] = movies
        }
    }

    fun getPersonMovieCredits(personId: Int, year: Int?): List<CastMovieCreditDTO>? {
        return if (personCredits?.id == personId) personCredits?.let {
            it.cast?.filter { credit ->
                year?.let {
                    credit.releaseDate?.startsWith(year.toString()) ?: false
                } ?: credit.releaseDate.isNullOrEmpty()
            }
                ?: emptyList()
        } else null
    }

    fun getPersonMovieCreditsYears(personId: Int): List<String?>? {
        return if (personCredits?.id == personId) personCredits?.let {
            it.cast?.map { credit -> credit.releaseDate } ?: emptyList()
        } else null
    }

    fun insert(credits: PersonMovieCreditsResponseDTO) {
        personCredits = credits
    }
}