package com.example.cinemapp.ui.main.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.ui.main.model.CastMember
import com.example.cinemapp.ui.main.model.Media
import com.example.cinemapp.ui.main.model.MovieDetails
import com.example.cinemapp.util.mappers.MovieDetailsMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val movieRepository: MovieRepository,
    private val movieDetailsMapper: MovieDetailsMapper
) : ViewModel() {

    data class State(
        val details: MovieDetails? = null,
        val cast: List<CastMember> = emptyList(),
        val media: List<Media> = emptyList()
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {

            val detailsCall = async {
                movieRepository.getMovieDetails(movieId)?.let { details ->
                    movieDetailsMapper.mapToMovieDetails(details, 500)
                }
            }
            val creditsCall = async {
                movieRepository.getMovieCredits(movieId)
                    ?.let { movieDetailsMapper.mapToMovieCredits(it, 500).cast }
                    ?: emptyList()
            }
            val imageCall = async {
                movieRepository.getImages(movieId)
                    ?.let { movieDetailsMapper.mapToImageList(it, 500) }
                    ?: emptyList()
            }
            val trailerCall = async {
                movieRepository.getVideos(movieId)
                    ?.let { chooseTrailer(movieDetailsMapper.mapToVideoList(it)) }
            }

            val newDetails: MovieDetails? = detailsCall.await()
            val newCredits: List<CastMember> = creditsCall.await()
            val newImages: List<Media.Image> = imageCall.await()
            val newTrailer: Media.Video? = trailerCall.await()

            _state.update {
                it.copy(
                    details = newDetails,
                    cast = newCredits,
                    media = (newDetails
                        ?.let { details -> listOf(Media.Image(details.backdropPath)) }
                        ?.plus(listOfNotNull(newTrailer)))
                        ?.plus(newImages.filter { media -> media.filePath != "" }) ?: emptyList()
                )
            }
        }
    }

    private fun chooseTrailer(videos: List<Media.Video>): Media.Video? {
        return videos.find {
            video -> video.type == "Trailer" && video.official && video.site == "YouTube"
        }
    }
}