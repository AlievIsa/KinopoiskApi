package com.example.kinopoiskapi.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.kinopoiskapi.R
import com.example.kinopoiskapi.domain.NetworkError
import com.example.kinopoiskapi.presentation.navigation.Screen
import com.example.kinopoiskapi.presentation.screens.home.items.MovieItem
import com.example.kinopoiskapi.presentation.screens.home.items.SearchMovieItem
import com.example.kinopoiskapi.presentation.screens.home.items.SearchQueryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(navController.getBackStackEntry(Screen.Home.route))
) {
    val context = LocalContext.current
    val searchText by homeViewModel.searchText.collectAsState()
    val isSearching by homeViewModel.isSearching.collectAsState()
    val searchQueryHistory by homeViewModel.searchQueryHistory.collectAsState()
    val movies = homeViewModel.moviePagingFlow.collectAsLazyPagingItems()
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshingByPullUp by remember {
        mutableStateOf(false)
    }
    val filters by homeViewModel.filters.collectAsState()

    Scaffold(
//        modifier = Modifier.pullToRefresh(
//            state = pullToRefreshState,
//            isRefreshing = isRefreshingByPullUp,
//            onRefresh = {
//                movies.refresh()
//            }
//        ),
        topBar = {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchText,
                        onQueryChange = homeViewModel::onSearchTextChange,
                        onSearch = { homeViewModel.onSearchClicked() },
                        expanded = isSearching,
                        onExpandedChange = { homeViewModel.onSearchBarActiveChange(it) },
                        placeholder = {
                            Text(stringResource(R.string.type_name))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.search_icon)
                            )
                        },
                        trailingIcon = {
                            if (isSearching) {
                                Icon(
                                    modifier = Modifier.clickable {
                                        if (searchText.isNotEmpty())
                                            homeViewModel.onSearchTextChange("")
                                        else
                                            homeViewModel.onSearchBarActiveChange(false)
                                    },
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.close_icon)
                                )
                            } else if (searchText.isEmpty()) {
                                Icon(
                                    modifier = Modifier.clickable {
                                        navController.navigate(Screen.Filter.route)
                                    },
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = stringResource(R.string.filter_icon)
                                )
                            }
                        }
                    )
                },
                expanded = isSearching,
                onExpandedChange = { homeViewModel.onSearchBarActiveChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    if (searchQueryHistory.isNotEmpty()) {
                        if (searchText.isBlank()) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = stringResource(R.string.search_history))
                                    Text(
                                        modifier = Modifier.clickable {
                                            homeViewModel.onDeleteAllSearchQueriesClick()
                                        },
                                        text = stringResource(R.string.delete_all),
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                        items(
                            items = searchQueryHistory,
                            key = { item -> item.id }) { searchQuery ->
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
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(16.dp)
                                )
                            }
                        }
                    } else {
                        if (movies.itemCount != 0) {
                            if (searchText.isNotBlank()) {
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
                                    text = stringResource(R.string.no_result)
                                )
                            }
                        }
                    }
                }
            }
//            SearchBar(
//                query = searchText,
//                onQueryChange = homeViewModel::onSearchTextChange,
//                onSearch = { homeViewModel.onSearchClicked() },
//                active = isSearching,
//                onActiveChange = { homeViewModel.onSearchBarActiveChange(it) },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 4.dp),
//                placeholder = {
//                    Text(stringResource(R.string.type_name))
//                },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = stringResource(R.string.search_icon)
//                    )
//                },
//                trailingIcon = {
//                    if (isSearching) {
//                        Icon(
//                            modifier = Modifier.clickable {
//                                if (searchText.isNotEmpty())
//                                    homeViewModel.onSearchTextChange("")
//                                else
//                                    homeViewModel.onSearchBarActiveChange(false)
//                            },
//                            imageVector = Icons.Default.Close,
//                            contentDescription = stringResource(R.string.close_icon)
//                        )
//                    } else if (searchText.isEmpty()) {
//                        Icon(
//                            modifier = Modifier.clickable {
//                                navController.navigate(Screen.Filter.route)
//                            },
//                            imageVector = Icons.Default.Tune,
//                            contentDescription = stringResource(R.string.filter_icon)
//                        )
//                    }
//                }
//            ) {
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.White)
//                ) {
//                    if (searchQueryHistory.isNotEmpty()) {
//                        if (searchText.isBlank()) {
//                            item {
//                                Row(
//                                    modifier = Modifier
//                                        .padding(8.dp)
//                                        .fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    Text(text = stringResource(R.string.search_history))
//                                    Text(
//                                        modifier = Modifier.clickable {
//                                            homeViewModel.onDeleteAllSearchQueriesClick()
//                                        },
//                                        text = stringResource(R.string.delete_all),
//                                        color = Color.Gray,
//                                        fontSize = 14.sp
//                                    )
//                                }
//                            }
//                        }
//                        items(
//                            items = searchQueryHistory,
//                            key = { item -> item.id }) { searchQuery ->
//                            SearchQueryItem(
//                                searchQuery,
//                                homeViewModel::onSearchTextChange,
//                                homeViewModel::onSearchClicked,
//                                homeViewModel::onDeleteSearchQuery
//                            )
//                        }
//                    }
//                    if (movies.loadState.refresh is LoadState.Loading) {
//                        item {
//                            Box(modifier = Modifier.fillMaxWidth()) {
//                                CircularProgressIndicator(
//                                    modifier = Modifier
//                                        .align(Alignment.Center)
//                                        .padding(16.dp)
//                                )
//                            }
//                        }
//                    } else {
//                        if (movies.itemCount != 0) {
//                            if (searchText.isNotBlank()) {
//                                items(count = movies.itemCount) { index ->
//                                    if (index <= homeViewModel.searchMoviesResultAmount) {
//                                        val movie = movies[index]
//                                        if (movie != null) {
//                                            Box(modifier = Modifier.clickable {
//                                                navController.navigate("${Screen.Movie.route}/${movie.id}")
//                                            }
//                                            ) {
//                                                SearchMovieItem(movie = movie)
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        } else {
//                            item {
//                                Text(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .align(Alignment.CenterHorizontally)
//                                        .padding(top = 24.dp),
//                                    text = stringResource(R.string.no_result)
//                                )
//                            }
//                        }
//                    }
//                }
//            }
        }
    ) { paddingValue ->
        LaunchedEffect(movies.loadState) {
            val loadState = movies.loadState.refresh
            if (loadState is LoadState.Error) {
                val errorMessage = when (val error = loadState.error) {
                    is NetworkError.NoInternetConnection -> context.getString(R.string.no_internet_connection)
                    is NetworkError.HttpError -> context.getString(
                        R.string.server_error,
                        error.code.toString(),
                        error.message
                    )
                    is NetworkError.UnknownError -> context.getString(
                        R.string.unexpected_error,
                        error.message
                    )
                    else -> context.getString(R.string.unknown_error_occurred)
                }
                Toast.makeText(
                    context,
                    errorMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        Box(
//            state = pullToRefreshState,
//            isRefreshing = isRefreshingByPullUp,
//            onRefresh = {
//                isRefreshingByPullUp = true
//                movies.refresh()
//                isRefreshingByPullUp = false
//            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {

            if (movies.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (filters != null) {
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            text = stringResource(R.string.temp_filters, filters!!)
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(count = movies.itemCount) { index ->
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
}