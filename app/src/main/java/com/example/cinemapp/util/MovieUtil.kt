package com.example.cinemapp.util

import com.example.cinemapp.BuildConfig
import com.example.cinemapp.data.CastMemberDTO
import com.example.cinemapp.data.GenreDTO
import com.example.cinemapp.data.ImageDTO
import com.example.cinemapp.data.MovieCreditsDTO
import com.example.cinemapp.data.MovieDTO
import com.example.cinemapp.data.MovieDetailsDTO
import com.example.cinemapp.data.PersonDetailsDTO
import com.example.cinemapp.data.CastMovieCreditDTO
import com.example.cinemapp.data.VideoDTO
import com.example.cinemapp.ui.main.model.CastMember
import com.example.cinemapp.ui.main.model.CastMovieCredit
import com.example.cinemapp.ui.main.model.Genre
import com.example.cinemapp.ui.main.model.Media
import com.example.cinemapp.ui.main.model.MovieCard
import com.example.cinemapp.ui.main.model.MovieCredits
import com.example.cinemapp.ui.main.model.MovieDetails
import com.example.cinemapp.ui.main.model.PersonDetails
import java.time.LocalDate

object MovieUtil {
    fun map(movie: MovieDTO): MovieCard {
        return MovieCard(
            movie.id ?: -1,
            mapImageURL(movie.posterPath, 400),
            movie.releaseDate?.let { LocalDate.parse(it) },
            movie.title ?: "",
            movie.voteAverage ?: 0f
        )
    }

    fun mapListMovie(movieList: List<MovieDTO>): List<MovieCard> =
        movieList.map { movie -> map(movie) }

    fun map(genre: GenreDTO): Genre {
        return Genre(
            genre.id ?: -1,
            genre.name ?: ""
        )
    }

    fun mapListGenre(genreList: List<GenreDTO>): List<Genre> =
        genreList.map { genre -> map(genre) }

    fun map(movieDetails: MovieDetailsDTO, backdropResolution: Int? = null): MovieDetails {
        return MovieDetails(
            movieDetails.id ?: -1,
            mapImageURL(movieDetails.backdropPath, backdropResolution),
            movieDetails.genres?.let { mapListGenre(it) } ?: emptyList(),
            movieDetails.overview ?: "",
            movieDetails.runtime ?: 0,
            movieDetails.title ?: ""
        )
    }

    private fun mapImageURL(
        imagePath: String?,
        resolution: Int? = null
    ): String {
        return imagePath?.let {
            "${BuildConfig.URL_BASE_IMAGE}${
                (resolution?.let { "w${resolution}" } ?: "original")
            }$imagePath/"
        } ?: ""
    }

    private fun mapYoutubeVideoURL(videoId: String?): String {
        return "<iframe " +
                "width=\"100%\" height=\"100%\" " +
                "style=\"top:0; left: 0; position: absolute;\"" +
                "src=\"https://www.youtube.com/embed/$videoId?si=lGrXTtU-7EZF6VLE\" " +
                "title=\"YouTube video player\" frameborder =\"0\" " +
                "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" " +
                "referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen>" +
                "</iframe>"
    }

    fun map(castMember: CastMemberDTO, resolution: Int? = null): CastMember {
        return CastMember(
            castMember.id ?: -1,
            castMember.name ?: "",
            mapImageURL(castMember.profilePath, resolution),
            castMember.character ?: ""
        )
    }

    fun mapListCastMember(cast: List<CastMemberDTO>, resolution: Int? = null): List<CastMember> =
        cast.map { member -> map(member, resolution) }

    fun map(creditsResponse: MovieCreditsDTO, resolution: Int? = null): MovieCredits {
        return MovieCredits(
            creditsResponse.id ?: -1,
            creditsResponse.cast?.let { mapListCastMember(creditsResponse.cast, resolution) }
                ?: emptyList()
        )
    }

    fun map(gender: Int): String {
        return when (gender) {
            1 -> "Female"
            2 -> "Male"
            3 -> "Non-binary"
            else -> "Not specified"
        }
    }

    fun map(person: PersonDetailsDTO, resolution: Int?): PersonDetails {
        return PersonDetails(
            person.id ?: -1,
            person.biography ?: "",
            person.birthday?.let { LocalDate.parse(it) },
            person.deathday?.let { LocalDate.parse(it) },
            person.gender?.let { map(it) } ?: "",
            person.name ?: "",
            mapImageURL(person.profilePath, resolution)
        )
    }

    fun map(image: ImageDTO, resolution: Int? = null): Media.Image {
        return Media.Image(mapImageURL(image.filePath, resolution))
    }

    fun mapListImages(images: List<ImageDTO>, resolution: Int? = null): List<Media.Image> =
        images.map { image -> map(image, resolution) }


    fun map(video: VideoDTO): Media.Video {
        return Media.Video(
            html = mapYoutubeVideoURL(video.key),
            site = video.site ?: "",
            type = video.type ?: "",
            official = video.official ?: false
        )
    }

    fun mapListVideos(videos: List<VideoDTO>): List<Media.Video> =
        videos.map { video -> map(video) }


    fun mapListYears(dates: List<String?>?): List<Int?> {
        return dates?.map { date ->
            if (date.isNullOrEmpty()) null
            else date.substring(0, 4).toInt()
        }?.distinct()?.sortedByDescending { year -> year ?: 0 } ?: emptyList()
    }

    fun mapCastMovieCredit(credit: CastMovieCreditDTO, resolution: Int? = null): CastMovieCredit {
        return CastMovieCredit(
            id = credit.id ?: -1,
            title = credit.title ?: "",
            posterPath = mapImageURL(credit.posterPath),
            character = credit.character ?: ""
        )
    }

    fun mapListCastMovieCredits(
        credits: List<CastMovieCreditDTO>,
        resolution: Int? = null
    ): List<CastMovieCredit> {
        return credits.map { mapCastMovieCredit(it, resolution) }
    }
}