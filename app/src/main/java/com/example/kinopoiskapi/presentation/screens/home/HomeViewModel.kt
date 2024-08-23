package com.example.kinopoiskapi.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.kinopoiskapi.data.mappers.toSearchQuery
import com.example.kinopoiskapi.domain.Movie
import com.example.kinopoiskapi.domain.Repository
import com.example.kinopoiskapi.domain.SearchQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SEARCH_RESULT_AMOUNT = 10
private const val SEARCH_QUERY_LIST_SIZE = 5

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchQueryHistory: MutableStateFlow<List<SearchQuery>> = MutableStateFlow(emptyList())
    val searchQueryHistory = _searchQueryHistory.asStateFlow()

    private val _moviePagingFlow: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty())
    val moviePagingFlow = _moviePagingFlow.asStateFlow()

    val searchMoviesResultAmount = SEARCH_RESULT_AMOUNT

    val genres = MutableStateFlow<List<String>>(emptyList())

    private val _type = MutableStateFlow(0)
    val type = _type.asStateFlow()
    private val _rateRange = MutableStateFlow(0..10)
    val rateRange = _rateRange.asStateFlow()
    private val _selectedYear = MutableStateFlow<String?>(null)
    val selectedYear = _selectedYear.asStateFlow()
    private val _selectedGenre = MutableStateFlow<String?>(null)
    val selectedGenre = _selectedGenre.asStateFlow()

    private val _filters = MutableStateFlow<String?>(null)
    val filters = _filters.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                _searchText.debounce(500L),
                _type,
                _rateRange,
                _selectedYear,
                _selectedGenre
            ) { query, type, rate, year, genre ->
                repository.getMovieFlowPagingData(
                    query = query,
                    type = type,
                    rate = rate,
                    year = year,
                    genre = genre
                )
            }.flatMapLatest { it }.cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _moviePagingFlow.value = pagingData
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
        viewModelScope.launch {
            genres.value = repository.getGenres().map { it.name ?: "" }
        }
        _moviePagingFlow
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onSearchBarActiveChange(isSearching: Boolean) {
        _isSearching.value = isSearching
        if (!_isSearching.value) {
            onSearchTextChange(_searchText.value)
        }
    }

    private fun upsertSearchQuery(query: String) = viewModelScope.launch {
        if (!_searchQueryHistory.value.any { it.text == query }) {
            if (_searchQueryHistory.value.size == SEARCH_QUERY_LIST_SIZE) {
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

    fun updateFilters(type: Int, typeName: String, genre: String?, year: String?, rateRange: IntRange) {
        _type.value = type
        _selectedGenre.value = genre
        _selectedYear.value = year
        _rateRange.value = rateRange

        val newFilters = mutableListOf<String>()

        if (type != 0) newFilters.add(typeName)
        if (genre != null) newFilters.add(genre)
        if (year != null) newFilters.add(year)
        if (rateRange != 0..10) newFilters.add("${rateRange.first}-${rateRange.last}")

        if (newFilters.isEmpty()) {
            _filters.value = null
        } else {
            _filters.value = newFilters.joinToString(", ")
        }
    }

    fun clearFilters() {
        _type.value = 0
        _selectedGenre.value = null
        _selectedYear.value = null
        _rateRange.value = 0..10
        _filters.value = null
    }
}