package com.example.cinemapp.util

import com.example.cinemapp.data.GenreDTO
import com.example.cinemapp.data.Movie
import com.example.cinemapp.data.MovieDetailsDTO
import com.example.cinemapp.ui.main.Genre
import com.example.cinemapp.ui.main.MovieCard
import com.example.cinemapp.ui.main.MovieDetails

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

    fun map(movieList: List<Movie>): List<MovieCard> = movieList.map { movie -> map(movie) }

    fun mapGenre(genre: GenreDTO): Genre {
        return Genre(
            genre.id,
            genre.name
        )
    }

    fun mapGenre(genreList: List<GenreDTO>): List<Genre> = genreList.map { genre -> mapGenre(genre) }

    fun map(movieDetails: MovieDetailsDTO): MovieDetails {
        return MovieDetails(
            movieDetails.id,
            movieDetails.backdropPath,
            movieDetails.genres?.let { mapGenre(it) },
            movieDetails.overview,
            movieDetails.runtime,
            movieDetails.title
        )
    }
}