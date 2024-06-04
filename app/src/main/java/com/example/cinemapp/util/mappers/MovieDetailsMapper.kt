package com.example.cinemapp.util.mappers

import com.example.cinemapp.data.CastMemberDTO
import com.example.cinemapp.data.GenreDTO
import com.example.cinemapp.data.ImageDTO
import com.example.cinemapp.data.MovieCreditsDTO
import com.example.cinemapp.data.MovieDetailsDTO
import com.example.cinemapp.data.VideoDTO
import com.example.cinemapp.ui.main.model.CastMember
import com.example.cinemapp.ui.main.model.Genre
import com.example.cinemapp.ui.main.model.Media
import com.example.cinemapp.ui.main.model.MovieCredits
import com.example.cinemapp.ui.main.model.MovieDetails

class MovieDetailsMapper(
    private val urlMapper: UrlMapper
) {
    private fun mapGenreDTOtoGenre(genre: GenreDTO): Genre {
        return Genre(
            genre.id ?: -1,
            genre.name ?: ""
        )
    }

    private fun mapGenreDTOListToGenreList(genreList: List<GenreDTO>): List<Genre> =
        genreList.map { genre -> mapGenreDTOtoGenre(genre) }


    fun mapMovieDetailsDTOToMovieDetails(
        movieDetails: MovieDetailsDTO,
        backdropResolution: Int? = null
    ): MovieDetails {
        return MovieDetails(
            movieDetails.id ?: -1,
            urlMapper.mapImageIdToBaseURL(movieDetails.backdropPath, backdropResolution),
            movieDetails.genres?.let { mapGenreDTOListToGenreList(it) } ?: emptyList(),
            movieDetails.overview ?: "",
            movieDetails.runtime ?: 0,
            movieDetails.title ?: ""
        )
    }

    private fun mapCastMemberDTOToCastMember(
        castMember: CastMemberDTO,
        resolution: Int? = null
    ): CastMember {
        return CastMember(
            castMember.id ?: -1,
            castMember.name ?: "",
            urlMapper.mapImageIdToBaseURL(castMember.profilePath, resolution),
            castMember.character ?: ""
        )
    }
    private fun mapCastMemberDTOListToCastMemberList(
        cast: List<CastMemberDTO>,
        resolution: Int? = null
    ): List<CastMember> =
        cast.map { member -> mapCastMemberDTOToCastMember(member, resolution) }

    fun mapMovieCreditsDTOToCastMemberList(
        creditsResponse: MovieCreditsDTO,
        resolution: Int? = null
    ): MovieCredits {
        return MovieCredits(
            creditsResponse.id ?: -1,
            creditsResponse.cast?.let {
                mapCastMemberDTOListToCastMemberList(
                    creditsResponse.cast,
                    resolution
                )
            } ?: emptyList()
        )
    }


    private fun mapImageDTOToImage(imageDTO: ImageDTO, resolution: Int? = null): Media.Image {
        return Media.Image(urlMapper.mapImageIdToBaseURL(imageDTO.filePath, resolution))
    }

    fun mapImageDTOListToImageList(
        images: List<ImageDTO>,
        resolution: Int? = null
    ): List<Media.Image> =
        images.map { image -> mapImageDTOToImage(image, resolution) }


    private fun mapVideoDTOToVideo(videoDTO: VideoDTO): Media.Video {
        return Media.Video(
            html = urlMapper.mapYoutubeURLToEmbedded(videoDTO.key),
            site = videoDTO.site ?: "",
            type = videoDTO.type ?: "",
            official = videoDTO.official ?: false
        )
    }

    fun mapVideoDTOListToVideoList(videos: List<VideoDTO>): List<Media.Video> =
        videos.map { video -> mapVideoDTOToVideo(video) }
}