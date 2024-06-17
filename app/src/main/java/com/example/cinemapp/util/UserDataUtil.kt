package com.example.cinemapp.util

import com.example.cinemapp.data.model.MovieDTO

class UserDataUtil {
    fun isMovieInList(movieId: Int?, movieList: List<MovieDTO>): Boolean {
        return movieList.firstOrNull { movie ->
            movie.id == movieId
        }?.let { true } ?: false
    }

    fun getUserRating(movieId: Int?, movieList: List<MovieDTO>): Int? {
        return movieList.firstOrNull { movie ->
                movie.id == movieId
            }?.rating
    }
}