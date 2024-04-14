package com.example.kinopoiskapi.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kinopoiskapi.presentation.navigation.Screen
import com.example.kinopoiskapi.presentation.screens.home.HomeScreen
import com.example.kinopoiskapi.presentation.screens.movie.MovieScreen
import com.example.kinopoiskapi.presentation.ui.theme.KinopoiskApiTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KinopoiskApiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = Screen.Home.route) {
                        composable(Screen.Home.route) {
                            HomeScreen(navController)
                        }
                        composable(Screen.Movie.route + "/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) {
                            MovieScreen(navController)
                        }
                    }
                }
            }
        }
    }
}