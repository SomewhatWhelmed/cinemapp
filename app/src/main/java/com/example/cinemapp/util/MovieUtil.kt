package com.example.cinemapp.util

import com.example.cinemapp.BuildConfig
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
            movie.posterPath?.let { mapImageURL(it, 500) },
            movie.releaseDate,
            movie.title,
            movie.voteAverage
        )
    }

    fun map(movieList: List<Movie>): List<MovieCard> = movieList.map { movie -> map(movie) }

    fun mapGenre(genre: GenreDTO): Genre {
        return Genre(
            genre.id ?: -1,
            genre.name ?: ""
        )
    }

    fun mapGenre(genreList: List<GenreDTO>): List<Genre> =
        genreList.map { genre -> mapGenre(genre) }

    fun map(movieDetails: MovieDetailsDTO, backdropResolution: Int? = null): MovieDetails {
        return MovieDetails(
            movieDetails.id ?: -1,
            movieDetails.backdropPath?.let { mapImageURL(it, backdropResolution) } ?: "",
            movieDetails.genres?.let { mapGenre(it) } ?: emptyList(),
            movieDetails.overview ?: "",
            movieDetails.runtime ?: 0,
            movieDetails.title ?: ""
        )
    }

    private fun mapImageURL(imagePath: String, resolution: Int? = null): String {
        return "${BuildConfig.URL_BASE_IMAGE}${
            (resolution?.let { "w${resolution}" } ?: "original")
        }$imagePath/"
    }
}