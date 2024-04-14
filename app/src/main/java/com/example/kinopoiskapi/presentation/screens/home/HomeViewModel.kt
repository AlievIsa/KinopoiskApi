package com.example.kinopoiskapi.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.kinopoiskapi.data.mappers.toMovie
import com.example.kinopoiskapi.data.mappers.toSearchQuery
import com.example.kinopoiskapi.domain.Movie
import com.example.kinopoiskapi.domain.Repository
import com.example.kinopoiskapi.domain.SearchQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SEARCH_RESULT_AMOUNT = 10

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _homeUIState = MutableStateFlow(HomeUIState())
    val homeUIState = _homeUIState.asStateFlow()

    private val _searchText = MutableStateFlow("")

    private val _isSearching = MutableStateFlow(false)

    private val _searchQueryHistory: MutableStateFlow<List<SearchQuery>> = MutableStateFlow(emptyList())
    val searchQueryHistory = _searchQueryHistory.asStateFlow()

    private val _moviePagingFlow: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty())
    val moviePagingFlow = _moviePagingFlow.asStateFlow()

    val searchMoviesResultAmount = SEARCH_RESULT_AMOUNT

    init {
        viewModelScope.launch {
            _searchText.debounce(500L)
                .collectLatest { query ->
                    repository.getMovieFlowPagingData(query).map { it.map { it.toMovie() } }
                        .cachedIn(viewModelScope)
                        .collectLatest {
                            _moviePagingFlow.value = it
                        }
                }
        }
        viewModelScope.launch {
            _searchText.collectLatest { query ->
                repository.getSearchQueries(query).map { it.map { it.toSearchQuery() } }
                    .collectLatest {
                        _searchQueryHistory.value = it.reversed()
                    }
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        _homeUIState.update {
            it.copy(searchText = text)
        }
    }

    fun onSearchBarActiveChange(isSearching: Boolean) {
        _isSearching.value = isSearching
        _homeUIState.update {
            it.copy(isSearching = isSearching)
        }
        if (!_isSearching.value) {
            onSearchTextChange(_searchText.value)
        }
    }

    private fun upsertSearchQuery(query: String) = viewModelScope.launch {
        if (!_searchQueryHistory.value.any { it.text == query }) {
            if (_searchQueryHistory.value.size == 20) {
                repository.deleteSearchQuery(_searchQueryHistory.value.last())
            }
            repository.upsertSearchQuery(query)
        }
    }

    fun onDeleteSearchQuery(searchQuery: SearchQuery) = viewModelScope.launch {
        repository.deleteSearchQuery(searchQuery)
    }

    fun onDeleteAllSearchQueriesClick() = viewModelScope.launch {
        repository.deleteAllSearchQueries()
    }

    fun onSearchClicked() {
        if (_searchText.value.isNotEmpty()) {
            upsertSearchQuery(_searchText.value.trim())
        }
        onSearchBarActiveChange(false)
    }


    data class HomeUIState(
        val searchText: String = "",
        val isSearching: Boolean = false
    )
}