package com.example.cinemapp.ui.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.ui.main.model.SearchCard
import com.example.cinemapp.ui.main.model.SearchType
import com.example.cinemapp.util.mappers.SearchMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val movieRepository: MovieRepository,
    private val searchMapper: SearchMapper
) : ViewModel() {

    data class State(
        val list: List<SearchCard> = emptyList(),
        val search: String = "",
        val pagesLoaded: Int = 0,
        val searchType: SearchType = SearchType.MOVIE
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state
    private var isPaging = false


    fun getMoviesNextPage(query: String = state.value.search) {
        getNextPage(SearchType.MOVIE, query)
    }

    fun getActorsNextPage(query: String = state.value.search) {
        getNextPage(SearchType.ACTOR, query)
    }

    fun getNextPage(
        searchType: SearchType = state.value.searchType,
        query: String = state.value.search
    ) {
        if (!isPaging) {
            setPagingRunning(true)
            val newPage =
                if (searchType == state.value.searchType && query == state.value.search)
                    state.value.pagesLoaded + 1
                else 1
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        pagesLoaded = newPage,
                        searchType = searchType,
                        search = query,
                        list = (if (newPage != 1) it.list else emptyList()).plus(
                            when (searchType) {
                                SearchType.MOVIE -> movieRepository.getSearchMovieList(
                                    query, newPage
                                )?.let { list ->
                                    searchMapper.mapToMovieCardList(list, 500)
                                }

                                SearchType.ACTOR -> movieRepository.getSearchPersonList(
                                    query, newPage
                                )?.let { list ->
                                    searchMapper.mapToPersonCardList(list, 500)
                                }
                            } ?: emptyList())
                    )
                }
            }
        }
    }

    fun setPagingRunning(isRunning: Boolean) {
        isPaging = isRunning
    }
}
