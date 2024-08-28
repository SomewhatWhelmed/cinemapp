package com.example.cinemapp.ui.main.actor_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.ui.main.model.CastMovieCredit
import com.example.cinemapp.ui.main.model.PersonDetails
import com.example.cinemapp.util.mappers.ActorDetailsMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActorDetailsViewModel(
    private val movieRepository: MovieRepository,
    private val actorDetailsMapper: ActorDetailsMapper
) : ViewModel() {

    data class State(
        val details: PersonDetails? = null,
        val creditYears: List<Int?> = emptyList(),
        val credits: List<CastMovieCredit> = emptyList(),
        val allCreditsShown: Boolean = true,
        val isLoading: Boolean = true
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    fun getCreditYears() = state.value.creditYears

    fun setupLoading() {
        _state.update {
            it.copy(isLoading = true)
        }
    }

    fun getPersonDetails(personId: Int) {
        viewModelScope.launch {
            val newCreditsYears = movieRepository.getPersonMovieCreditsYears(personId)?.let {
                actorDetailsMapper.mapToDescendingYears(it)
            } ?: emptyList()


            _state.update {
                it.copy(
                    details = movieRepository.getPersonDetails(personId = personId)
                        ?.let { details ->
                            actorDetailsMapper.mapToPersonDetails(
                                details,
                                500
                            )
                        },
                    creditYears = newCreditsYears,
                    credits = movieRepository.getPersonMovieCredits(personId = personId)?.let { credits ->
                        actorDetailsMapper.mapToCastMovieCreditList(
                            credits.sortedByDescending { credit -> credit.releaseDate }
                        )
                    } ?: emptyList(),
                    isLoading = false,
                    allCreditsShown = true
                )
            }
        }
    }

    fun getCreditsFromYear(personId: Int, year: Int?, getAll: Boolean, moviesOnly: Boolean) {
        viewModelScope.launch {
            val credits = movieRepository.getPersonMovieCredits(personId, year, getAll)
                ?.let { credits ->
                    actorDetailsMapper.mapToCastMovieCreditList(
                        credits.sortedByDescending { credit -> credit.releaseDate }
                    )
                } ?: emptyList()
            _state.update {
                it.copy(
                    credits = if (moviesOnly) credits.filter { credit ->
                        credit.character.contains("Self", false).not()
                    } else credits,
                    allCreditsShown = getAll
                )
            }
        }
    }

}