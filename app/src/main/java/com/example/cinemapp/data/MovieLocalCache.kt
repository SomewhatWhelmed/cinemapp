package com.example.cinemapp.data

import android.util.Log
import com.example.cinemapp.data.model.CastMovieCreditDTO
import com.example.cinemapp.data.model.MovieDTO
import com.example.cinemapp.data.model.MovieResponseDTO
import com.example.cinemapp.data.model.PersonMovieCreditsResponseDTO

class MovieLocalCache {

    private val upcomingCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private var upcomingPages: Int? = 1
    private val popularCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private var popularPages: Int? = 1
    private val topRatedCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private var topRatedPages: Int? = 1

    private val favoriteCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private var favoritePages: Int? = 1
    private val watchlistCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private var watchlistPages: Int? = 1
    private val ratedCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private var ratedPages: Int? = 1

    private var personCredits: PersonMovieCreditsResponseDTO? = null

    fun getMovieList(
        movieListType: MovieRepository.MovieListType,
        page: Int = 1
    ): MovieResponseDTO? {
        Log.i("CACHE", "Searching Cache...")
        return when (movieListType) {
            MovieRepository.MovieListType.UPCOMING -> upcomingCache[page]?.let {
                MovieResponseDTO(
                    page = page,
                    results = it,
                    totalPages = upcomingPages
                )
            }

            MovieRepository.MovieListType.POPULAR -> popularCache[page]?.let {
                MovieResponseDTO(
                    page = page,
                    results = it,
                    totalPages = popularPages
                )
            }

            MovieRepository.MovieListType.TOP_RATED -> topRatedCache[page]?.let {
                MovieResponseDTO(
                    page = page,
                    results = it,
                    totalPages = topRatedPages
                )
            }

            MovieRepository.MovieListType.FAVORITE -> favoriteCache[page]?.let {
                MovieResponseDTO(
                    page = page,
                    results = it,
                    totalPages = favoritePages
                )
            }

            MovieRepository.MovieListType.WATCHLIST -> watchlistCache[page]?.let {
                MovieResponseDTO(
                    page = page,
                    results = it,
                    totalPages = watchlistPages
                )
            }

            MovieRepository.MovieListType.RATED -> ratedCache[page]?.let {
                MovieResponseDTO(
                    page = page,
                    results = it,
                    totalPages = ratedPages
                )
            }
        }
    }

    fun insert(
        movieListType: MovieRepository.MovieListType,
        page: Int = 1,
        movies: ArrayList<MovieDTO>,
        totalPages: Int? = 1
    ) {
        Log.i("CACHE", "Inserting into Cache")
        when (movieListType) {
            MovieRepository.MovieListType.UPCOMING -> {
                upcomingCache[page] = movies
                upcomingPages = totalPages
            }

            MovieRepository.MovieListType.POPULAR -> {
                popularCache[page] = movies
                popularPages = totalPages
            }

            MovieRepository.MovieListType.TOP_RATED -> {
                topRatedCache[page] = movies
                topRatedPages = totalPages
            }

            MovieRepository.MovieListType.FAVORITE -> {
                favoriteCache[page] = movies
                favoritePages = totalPages
            }

            MovieRepository.MovieListType.WATCHLIST -> {
                watchlistCache[page] = movies
                watchlistPages = totalPages
            }

            MovieRepository.MovieListType.RATED -> {
                ratedCache[page] = movies
                ratedPages = totalPages
            }
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

    fun clearProfileCache() {
        favoriteCache.clear()
        watchlistCache.clear()
        ratedCache.clear()
    }

    fun clearCache(listType: MovieRepository.MovieListType) {
        when(listType) {
            MovieRepository.MovieListType.UPCOMING -> upcomingCache.clear()
            MovieRepository.MovieListType.POPULAR -> popularCache.clear()
            MovieRepository.MovieListType.TOP_RATED -> topRatedCache.clear()
            MovieRepository.MovieListType.FAVORITE -> favoriteCache.clear()
            MovieRepository.MovieListType.WATCHLIST -> watchlistCache.clear()
            MovieRepository.MovieListType.RATED -> ratedCache.clear()
        }
    }
}