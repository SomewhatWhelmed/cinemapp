package com.example.cinemapp.data

import android.util.Log
import com.example.cinemapp.data.model.CastMovieCreditDTO
import com.example.cinemapp.data.model.MovieDTO
import com.example.cinemapp.data.model.MovieResponseDTO
import com.example.cinemapp.data.model.PersonMovieCreditsResponseDTO

class MovieLocalCache {

    private val upcomingCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private var upcomingPagesTotal: Int? = 1
    private val popularCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private var popularPagesTotal: Int? = 1
    private val topRatedCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private var topRatedPagesTotal: Int? = 1

    private val favoriteCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private var favoritePagesTotal: Int? = 1
    private val watchlistCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private var watchlistPagesTotal: Int? = 1
    private val ratedCache = mutableMapOf<Int, ArrayList<MovieDTO>>()
    private var ratedPagesTotal: Int? = 1

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
                    totalPages = upcomingPagesTotal
                )
            }

            MovieRepository.MovieListType.POPULAR -> popularCache[page]?.let {
                MovieResponseDTO(
                    page = page,
                    results = it,
                    totalPages = popularPagesTotal
                )
            }

            MovieRepository.MovieListType.TOP_RATED -> topRatedCache[page]?.let {
                MovieResponseDTO(
                    page = page,
                    results = it,
                    totalPages = topRatedPagesTotal
                )
            }

            MovieRepository.MovieListType.FAVORITE -> favoriteCache[page]?.let {
                MovieResponseDTO(
                    page = page,
                    results = it,
                    totalPages = favoritePagesTotal
                )
            }

            MovieRepository.MovieListType.WATCHLIST -> watchlistCache[page]?.let {
                MovieResponseDTO(
                    page = page,
                    results = it,
                    totalPages = watchlistPagesTotal
                )
            }

            MovieRepository.MovieListType.RATED -> ratedCache[page]?.let {
                MovieResponseDTO(
                    page = page,
                    results = it,
                    totalPages = ratedPagesTotal
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
                upcomingPagesTotal = totalPages
            }

            MovieRepository.MovieListType.POPULAR -> {
                popularCache[page] = movies
                popularPagesTotal = totalPages
            }

            MovieRepository.MovieListType.TOP_RATED -> {
                topRatedCache[page] = movies
                topRatedPagesTotal = totalPages
            }

            MovieRepository.MovieListType.FAVORITE -> {
                favoriteCache[page] = movies
                favoritePagesTotal = totalPages
            }

            MovieRepository.MovieListType.WATCHLIST -> {
                watchlistCache[page] = movies
                watchlistPagesTotal = totalPages
            }

            MovieRepository.MovieListType.RATED -> {
                ratedCache[page] = movies
                ratedPagesTotal = totalPages
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

    fun clearAllCache() {
        clearProfileCache()
        upcomingCache.clear()
        popularCache.clear()
        topRatedCache.clear()
    }
}