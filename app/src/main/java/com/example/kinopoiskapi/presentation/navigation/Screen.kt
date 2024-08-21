package com.example.kinopoiskapi.presentation.navigation

sealed class Screen(val route: String) {

    data object Home: Screen("Home")

    data object Movie: Screen("Movie")

    data object Filter: Screen("Filter")
}