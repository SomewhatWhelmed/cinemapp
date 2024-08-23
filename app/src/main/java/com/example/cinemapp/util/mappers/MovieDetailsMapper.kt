package com.example.cinemapp.util.mappers

import com.example.cinemapp.data.model.CastMemberDTO
import com.example.cinemapp.data.model.CrewMemberDTO
import com.example.cinemapp.data.model.GenreDTO
import com.example.cinemapp.data.model.ImageDTO
import com.example.cinemapp.data.model.MovieCreditsDTO
import com.example.cinemapp.data.model.MovieDetailsDTO
import com.example.cinemapp.data.model.VideoDTO
import com.example.cinemapp.ui.main.model.CastMember
import com.example.cinemapp.ui.main.model.CrewMember
import com.example.cinemapp.ui.main.model.Genre
import com.example.cinemapp.ui.main.model.Media
import com.example.cinemapp.ui.main.model.MovieCredits
import com.example.cinemapp.ui.main.model.MovieDetails

class MovieDetailsMapper(
    private val mediaUrlMapper: MediaUrlMapper
) {
    private fun mapToGenre(genre: GenreDTO): Genre {
        return Genre(
            id = genre.id ?: -1,
            name = genre.name ?: ""
        )
    }

    private fun mapToGenreList(genreList: List<GenreDTO>): List<Genre> =
        genreList.map { genre -> mapToGenre(genre) }


    fun mapToMovieDetails(
        movieDetails: MovieDetailsDTO,
        backdropResolution: Int? = null
    ): MovieDetails {
        return MovieDetails(
            id = movieDetails.id ?: -1,
            backdropPath = mediaUrlMapper.mapImageIdToBaseURL(movieDetails.backdropPath, backdropResolution),
            genres = movieDetails.genres?.let { mapToGenreList(it) } ?: emptyList(),
            overview = movieDetails.overview ?: "",
            runtime = movieDetails.runtime ?: 0,
            title = movieDetails.title ?: "",
            voteAverage = movieDetails.voteAverage ?: 0f
        )
    }

    private fun mapToCastMember(
        castMember: CastMemberDTO,
        resolution: Int? = null
    ): CastMember {
        return CastMember(
            id = castMember.id ?: -1,
            name = castMember.name ?: "",
            profilePath = mediaUrlMapper.mapImageIdToBaseURL(castMember.profilePath, resolution),
            character = castMember.character ?: ""
        )
    }
    private fun mapToCastMemberList(
        cast: List<CastMemberDTO>,
        resolution: Int? = null
    ): List<CastMember> =
        cast.map { member -> mapToCastMember(member, resolution) }

    private fun mapToCrewMember(
        castMember: CrewMemberDTO
    ): CrewMember {
        return CrewMember(
            id = castMember.id ?: -1,
            name = castMember.name ?: "",
            department = castMember.department ?: "",
            job = castMember.job ?: ""
        )
    }
    private fun mapToCrewMemberList(
        crew: List<CrewMemberDTO>
    ): List<CrewMember> =
        crew.map { member -> mapToCrewMember(member) }

    fun mapToMovieCredits(
        creditsResponse: MovieCreditsDTO,
        resolution: Int? = null
    ): MovieCredits {
        return MovieCredits(
            id = creditsResponse.id ?: -1,
            cast = creditsResponse.cast?.let {
                mapToCastMemberList(
                    cast = creditsResponse.cast,
                    resolution = resolution
                )
            } ?: emptyList(),
            crew = creditsResponse.crew?.let { crew ->
                mapToCrewMemberList(
                    crew = crew
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