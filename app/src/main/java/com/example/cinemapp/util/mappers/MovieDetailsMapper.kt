package com.example.cinemapp.util.mappers

import com.example.cinemapp.data.model.CastMemberDTO
import com.example.cinemapp.data.model.GenreDTO
import com.example.cinemapp.data.model.ImageDTO
import com.example.cinemapp.data.model.MovieCreditsDTO
import com.example.cinemapp.data.model.MovieDetailsDTO
import com.example.cinemapp.data.model.VideoDTO
import com.example.cinemapp.ui.main.model.CastMember
import com.example.cinemapp.ui.main.model.Genre
import com.example.cinemapp.ui.main.model.Media
import com.example.cinemapp.ui.main.model.MovieCredits
import com.example.cinemapp.ui.main.model.MovieDetails

class MovieDetailsMapper(
    private val mediaUrlMapper: MediaUrlMapper
) {
    private fun mapToGenre(genre: GenreDTO): Genre {
        return Genre(
            genre.id ?: -1,
            genre.name ?: ""
        )
    }

    private fun mapToGenreList(genreList: List<GenreDTO>): List<Genre> =
        genreList.map { genre -> mapToGenre(genre) }


    fun mapToMovieDetails(
        movieDetails: MovieDetailsDTO,
        backdropResolution: Int? = null
    ): MovieDetails {
        return MovieDetails(
            movieDetails.id ?: -1,
            mediaUrlMapper.mapImageIdToBaseURL(movieDetails.backdropPath, backdropResolution),
            movieDetails.genres?.let { mapToGenreList(it) } ?: emptyList(),
            movieDetails.overview ?: "",
            movieDetails.runtime ?: 0,
            movieDetails.title ?: "",
            movieDetails.voteAverage ?: 0f
        )
    }

    private fun mapToCastMember(
        castMember: CastMemberDTO,
        resolution: Int? = null
    ): CastMember {
        return CastMember(
            castMember.id ?: -1,
            castMember.name ?: "",
            mediaUrlMapper.mapImageIdToBaseURL(castMember.profilePath, resolution),
            castMember.character ?: ""
        )
    }
    private fun mapToCastMemberList(
        cast: List<CastMemberDTO>,
        resolution: Int? = null
    ): List<CastMember> =
        cast.map { member -> mapToCastMember(member, resolution) }

    fun mapToMovieCredits(
        creditsResponse: MovieCreditsDTO,
        resolution: Int? = null
    ): MovieCredits {
        return MovieCredits(
            creditsResponse.id ?: -1,
            creditsResponse.cast?.let {
                mapToCastMemberList(
                    creditsResponse.cast,
                    resolution
                )
            } ?: emptyList()
        )
    }


    private fun mapToImage(imageDTO: ImageDTO, resolution: Int? = null): Media.Image {
        return Media.Image(mediaUrlMapper.mapImageIdToBaseURL(imageDTO.filePath, resolution))
    }

    fun mapToImageList(
        images: List<ImageDTO>,
        resolution: Int? = null
    ): List<Media.Image> =
        images.map { image -> mapToImage(image, resolution) }


    private fun mapToVideo(videoDTO: VideoDTO): Media.Video {
        return Media.Video(
            html = mediaUrlMapper.mapYoutubeURLToEmbedded(videoDTO.key),
            site = videoDTO.site ?: "",
            type = videoDTO.type ?: "",
            official = videoDTO.official ?: false
        )
    }

    fun mapToVideoList(videos: List<VideoDTO>): List<Media.Video> =
        videos.map { video -> mapToVideo(video) }



}