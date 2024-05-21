package com.example.cinemapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.ui.main.model.CastMember
import com.example.cinemapp.ui.main.model.Media
import com.example.cinemapp.ui.main.model.MovieDetails
import com.example.cinemapp.util.MovieUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val movieRepository: MovieRepository
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
                    MovieUtil.map(details, 500)
                }
            }
            val creditsCall = async {
                movieRepository.getCredits(movieId)
                    ?.let { MovieUtil.map(it, 500).cast }
                    ?: emptyList()
            }
            val mediaCall = async {
                movieRepository.getImages(movieId)
                    ?.let { MovieUtil.mapMedia(it, 500) }
                    ?: emptyList()
            }
            val trailerCall = async {
                movieRepository.getVideos(movieId)
                    ?.let { chooseTrailer(MovieUtil.mapMedia(it)) }
            }

            val newDetails: MovieDetails? = detailsCall.await()
            val newCredits: List<CastMember> = creditsCall.await()
            val newMedia: List<Media> = mediaCall.await()
            val newTrailer: Media.Video? = trailerCall.await()

            _state.update {
                it.copy(
                    details = newDetails,
                    cast = newCredits,
                    media = listOf(Media.Image(newDetails?.backdropPath ?: "")) +
                            listOfNotNull(newTrailer) +
                            newMedia
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