package com.example.kinopoiskapi.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.kinopoiskapi.domain.NetworkError
import com.example.kinopoiskapi.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val homeUIState by homeViewModel.homeUIState.collectAsState()
//    val searchText by homeViewModel.searchText.collectAsState()
//    val isSearching by homeViewModel.isSearching.collectAsState()
    val searchQueryHistory = homeViewModel.searchQueryHistory.collectAsState()
    val movies = homeViewModel.moviePagingFlow.collectAsLazyPagingItems()

    Scaffold(
      topBar = {
          SearchBar(
              query = homeUIState.searchText,
              onQueryChange = homeViewModel::onSearchTextChange,
              onSearch = { homeViewModel.onSearchClicked() },
              active = homeUIState.isSearching,
              onActiveChange = { homeViewModel.onSearchBarActiveChange(it) },
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(4.dp),
              placeholder = {
                  Text("Введите название")
              },
              leadingIcon = {
                  Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
              },
              trailingIcon = {
                  if (homeUIState.isSearching) {
                      Icon(
                          modifier = Modifier.clickable {
                              if (homeUIState.searchText.isNotEmpty())
                                  homeViewModel.onSearchTextChange("")
                              else
                                  homeViewModel.onSearchBarActiveChange(false)
                          },
                          imageVector = Icons.Default.Close,
                          contentDescription = "Close Icon"
                      )
                  }
              },
          ) {
              LazyColumn(modifier = Modifier
                  .fillMaxSize()
                  .background(Color.White)) {
                  if (searchQueryHistory.value.isNotEmpty()) {
                      if (homeUIState.searchText.isBlank()) {
                          item {
                              Row(
                                  modifier = Modifier
                                      .padding(8.dp)
                                      .fillMaxWidth(),
                                  horizontalArrangement = Arrangement.SpaceBetween
                              ) {
                                  Text(text = "История поиска:")
                                  Text(
                                      modifier = Modifier.clickable {
                                          homeViewModel.onDeleteAllSearchQueriesClick()
                                      },
                                      text = "Очистить",
                                      color = Color.Gray,
                                      fontSize = 14.sp
                                  )
                              }
                          }
                      }
                      items(items = searchQueryHistory.value, key = { item -> item.id }) { searchQuery ->
                          SearchQueryItem(
                              searchQuery,
                              homeViewModel::onSearchTextChange,
                              homeViewModel::onSearchClicked,
                              homeViewModel::onDeleteSearchQuery
                          )
                      }
                  }
                  if (movies.loadState.refresh is LoadState.Loading) {
                      item {
                          Box(modifier = Modifier.fillMaxWidth()) {
                              CircularProgressIndicator(modifier = Modifier
                                  .align(Alignment.Center)
                                  .padding(16.dp))
                          }
                      }
                  } else {
                      if (movies.itemCount != 0) {
                          if (homeUIState.searchText.isNotBlank()) {
                              items(count = movies.itemCount) { index ->
                                  if (index <= homeViewModel.searchMoviesResultAmount) {
                                      val movie = movies[index]
                                      if (movie != null) {
                                          Box(modifier = Modifier.clickable {
                                              navController.navigate("${Screen.Movie.route}/${movie.id}")
                                          }
                                          ) {
                                              SearchMovieItem(movie = movie)
                                          }
                                      }
                                  }
                              }
                          }
                      } else {
                          item {
                              Text(
                                  modifier = Modifier
                                      .fillMaxWidth()
                                      .align(Alignment.CenterHorizontally)
                                      .padding(top = 24.dp),
                                  text = "Нет подходящих результатов")
                          }
                      }
                  }
              }
          }
      }
    ) {
        LaunchedEffect(key1 = movies.loadState) {
            val loadState = movies.loadState.refresh
            if (loadState is LoadState.Error) {
                val errorMessage = when (val error = loadState.error) {
                    is NetworkError.NoInternetConnection -> "No internet connection.\nPlease check your network."
                    is NetworkError.HttpError -> "Server error ${error.code}: ${error.message}"
                    is NetworkError.UnknownError -> "Unexpected error: ${error.message}"
                    else -> "Unknown error occurred."
                }
                Toast.makeText(
                    context,
                    errorMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            if (movies.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(count = movies.itemCount) {index ->
                        val movie = movies[index]
                        if (movie != null) {
                            Box(modifier = Modifier.clickable {
                                navController.navigate("${Screen.Movie.route}/${movie.id}")
                            }
                            ) {
                                MovieItem(movie = movie)
                            }
                        }
                    }
                    item {
                        if (movies.loadState.append is LoadState.Loading) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}