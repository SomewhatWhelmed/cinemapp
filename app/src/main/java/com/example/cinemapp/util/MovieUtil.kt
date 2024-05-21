package com.example.cinemapp.util

import com.example.cinemapp.BuildConfig
import com.example.cinemapp.data.CastMemberDTO
import com.example.cinemapp.data.GenreDTO
import com.example.cinemapp.data.ImageDTO
import com.example.cinemapp.data.MovieCreditsDTO
import com.example.cinemapp.data.MovieDTO
import com.example.cinemapp.data.MovieDetailsDTO
import com.example.cinemapp.data.VideoDTO
import com.example.cinemapp.ui.main.model.CastMember
import com.example.cinemapp.ui.main.model.Genre
import com.example.cinemapp.ui.main.model.Media
import com.example.cinemapp.ui.main.model.MovieCard
import com.example.cinemapp.ui.main.model.MovieCredits
import com.example.cinemapp.ui.main.model.MovieDetails

object MovieUtil {
    fun map(movie: MovieDTO): MovieCard {
        return MovieCard(
            movie.id,
            mapImageURL(movie.posterPath, 500),
            movie.releaseDate,
            movie.title,
            movie.voteAverage
        )
    }

    fun map(movieList: List<MovieDTO>): List<MovieCard> = movieList.map { movie -> map(movie) }

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
            mapImageURL(movieDetails.backdropPath, backdropResolution),
            movieDetails.genres?.let { mapGenre(it) } ?: emptyList(),
            movieDetails.overview ?: "",
            movieDetails.runtime ?: 0,
            movieDetails.title ?: ""
        )
    }

    private fun mapImageURL(imagePath: String?, resolution: Int? = null): String {
        return imagePath?.let {
            "${BuildConfig.URL_BASE_IMAGE}${
                (resolution?.let { "w${resolution}" } ?: "original")
            }$imagePath/"
        } ?: ""
    }

    private fun mapYoutubeVideoURL(videoId: String?): String {
        return "<iframe " +
                "width=\"100%\" height=\"100%\" " +
                "src=\"https://www.youtube.com/embed/$videoId?si=lGrXTtU-7EZF6VLE\" " +
                "title=\"YouTube video player\" frameborder=\"0\" " +
                "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" " +
                "referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen>" +
                "</iframe>"
    }

    fun mapCast(castMember: CastMemberDTO, resolution: Int? = null): CastMember {
        return CastMember(
            castMember.id ?: -1,
            castMember.name ?: "",
            mapImageURL(castMember.profilePath, resolution),
            castMember.character ?: ""
        )
    }

    fun mapCast(cast: List<CastMemberDTO>, resolution: Int? = null): List<CastMember> =
        cast.map { member -> mapCast(member, resolution) }

    fun map(creditsResponse: MovieCreditsDTO, resolution: Int? = null): MovieCredits {
        return MovieCredits(
            creditsResponse.id ?: -1,
            creditsResponse.cast?.let { mapCast(creditsResponse.cast, resolution) } ?: emptyList()
        )
    }

    fun mapMedia(image: ImageDTO, resolution: Int? = null): Media.Image {
        return Media.Image(mapImageURL(image.filePath, resolution))
    }

    fun mapMedia(images: List<ImageDTO>, resolution: Int? = null): List<Media.Image> =
        images.map { image -> mapMedia(image, resolution) }


    fun mapMedia(video: VideoDTO): Media.Video {
        return Media.Video(
            html = mapYoutubeVideoURL(video.key),
            site = video.site ?: "",
            type = video.type ?: "",
            official = video.official ?: false
        )
    }

    fun mapMedia(videos: List<VideoDTO>): List<Media.Video> = videos.map { video -> mapMedia(video) }

}