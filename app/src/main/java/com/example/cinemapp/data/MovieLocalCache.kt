package com.example.cinemapp.data

import android.util.Log
import com.example.cinemapp.data.model.CastMovieCreditDTO
import com.example.cinemapp.data.model.MovieDTO
import com.example.cinemapp.data.model.PersonMovieCreditsResponseDTO

class MovieLocalCache {

    private val upcomingCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private val popularCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private val topRatedCache = mutableMapOf<Int, ArrayList<MovieDTO>>()

    private val favouriteCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private val watchlistCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private val ratedCache = mutableMapOf<Int, ArrayList<MovieDTO>>()

    private var personCredits: PersonMovieCreditsResponseDTO? = null

    fun getMovieList(movieListType: MovieRepository.MovieListType, page: Int = 1): ArrayList<MovieDTO>? {
        Log.i("CACHE", "Searching Cache...")
        return when (movieListType) {
            MovieRepository.MovieListType.UPCOMING -> upcomingCache[page]
            MovieRepository.MovieListType.POPULAR -> popularCache[page]
            MovieRepository.MovieListType.TOP_RATED -> topRatedCache[page]
            MovieRepository.MovieListType.FAVORITE -> favouriteCache[page]
            MovieRepository.MovieListType.WATCHLIST -> watchlistCache[page]
            MovieRepository.MovieListType.RATED -> ratedCache[page]
        }
    }

    fun insert(movieListType: MovieRepository.MovieListType, page: Int = 1, movies: ArrayList<MovieDTO>) {
        Log.i("CACHE", "Inserting into Cache")
        when (movieListType) {
            MovieRepository.MovieListType.UPCOMING -> upcomingCache[page] = movies
            MovieRepository.MovieListType.POPULAR -> popularCache[page] = movies
            MovieRepository.MovieListType.TOP_RATED -> topRatedCache[page] = movies
            MovieRepository.MovieListType.FAVORITE -> favouriteCache[page] = movies
            MovieRepository.MovieListType.WATCHLIST -> watchlistCache[page] = movies
            MovieRepository.MovieListType.RATED -> ratedCache[page] = movies
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