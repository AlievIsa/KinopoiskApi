package com.example.kinopoiskapi.presentation.screens.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoiskapi.domain.Movie
import com.example.kinopoiskapi.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: Repository,
    private val state: SavedStateHandle
): ViewModel() {

    private val movieId: Int = checkNotNull(state["id"])
    private val _movie: MutableStateFlow<Movie> = MutableStateFlow(Movie())
    val movie = _movie.asStateFlow()

    init {
        viewModelScope.launch {
            _movie.value = repository.getMovieById(movieId)
        }
    }
}