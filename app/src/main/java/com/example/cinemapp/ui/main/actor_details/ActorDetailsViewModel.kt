package com.example.cinemapp.ui.main.actor_details

import android.util.Log
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
        val isLoading: Boolean = true
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

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
                    credits = if (newCreditsYears.isNotEmpty())
                        movieRepository.getPersonMovieCredits(personId, newCreditsYears[0])
                            ?.let { credits ->
                                actorDetailsMapper.mapToCastMovieCreditList(
                                    credits
                                )
                            } ?: emptyList()
                    else emptyList(),
                    isLoading = false
                )
            }
        }
    }

    fun getCreditsFromYear(personId: Int, year: Int?) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    credits = movieRepository.getPersonMovieCredits(personId, year)
                        ?.let { credits ->
                            actorDetailsMapper.mapToCastMovieCreditList(
                                credits
                            )
                        } ?: emptyList()
                )
            }
        }
    }

}