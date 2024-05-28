package com.example.cinemapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.ui.main.model.CastMovieCredit
import com.example.cinemapp.ui.main.model.PersonDetails
import com.example.cinemapp.util.MovieUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActorDetailsViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    data class State(
        val details: PersonDetails? = null,
        val creditYears: List<Int?> = emptyList()
    )

    data class StateCredits(
        val credits: List<CastMovieCredit> = emptyList()
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private val _stateCredits = MutableStateFlow(StateCredits())
    val stateCredits: StateFlow<StateCredits> = _stateCredits.asStateFlow()


    fun getPersonDetails(personId: Int) {
        viewModelScope.launch {

            val newCreditsYears = async {
                movieRepository.getPersonMovieCreditsYears(personId)?.let {
                    MovieUtil.mapListYears(it)
                } ?: emptyList()
            }.await()


            _state.update {
                it.copy(
                    details = movieRepository.getPersonDetails(personId = personId)
                        ?.let { details -> MovieUtil.map(details, 500) },
                    creditYears = newCreditsYears
                )
            }
            _stateCredits.update {
                StateCredits(
                    if (newCreditsYears.isNotEmpty())
                        movieRepository.getPersonMovieCredits(personId, newCreditsYears[0])
                            ?.let { credits ->
                                MovieUtil.mapListCastMovieCredits(credits)
                            } ?: emptyList()
                    else emptyList()
                )
            }
        }
    }

    fun getCreditsFromYear(personId: Int, year: Int?) {
        viewModelScope.launch {
            _stateCredits.update {
                StateCredits(
                    movieRepository.getPersonMovieCredits(personId, year)
                        ?.let { credits ->
                            MovieUtil.mapListCastMovieCredits(credits)
                        } ?: emptyList()
                )
            }
        }
    }

}