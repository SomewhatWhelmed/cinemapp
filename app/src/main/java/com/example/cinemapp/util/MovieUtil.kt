package com.example.cinemapp.util

import com.example.cinemapp.data.Movie
import com.example.cinemapp.data.MovieCard

object MovieUtil {
    fun map(movie: Movie): MovieCard {
        return MovieCard(
            movie.id,
            movie.posterPath,
            movie.releaseDate,
            movie.title,
            movie.voteAverage
        )
    }

    fun map(movieList: List<Movie>) = movieList.map { movie -> map(movie) }
}