package com.example.kinopoiskapi.presentation.screens.movie

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kinopoiskapi.domain.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    navController: NavController,
    movieViewModel: MovieViewModel = hiltViewModel()
) {
    val movie = movieViewModel.movie.collectAsState().value
    BottomSheetScaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .shadow(elevation = 2.dp)
                    .fillMaxWidth(),
                title = {
                    Text(
                        fontSize = 16.sp,
                        maxLines = 1,
                        text = movie.name ?: "<Без названия>",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        sheetContent = {
            Content(movie)
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues).fillMaxWidth()
        ) {
            movie.logo?.url?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Movie Poster",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            movie.backdrop?.url?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Movie Poster",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        topBar = {
//            TopAppBar(
//                modifier = Modifier
//                    .shadow(elevation = 2.dp)
//                    .fillMaxWidth(),
//                title = {
//                    Text(
//                        fontSize = 16.sp,
//                        maxLines = 1,
//                        text = movie.name,
//                        fontWeight = FontWeight.Bold
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = "Back"
//                        )
//                    }
//                }
//            )
//        },
//    ) { paddingValues ->
//        ModalBottomSheet(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues),
//            onDismissRequest = { /*TODO*/ }) {
//            Content(movie)
//        }
//    }
}

@Composable
fun Content(movie: Movie) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Страна, жанр и год
        Text(
            modifier = Modifier.padding(8.dp),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(movie.country.orEmpty())
                }
                append(" | ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(movie.genre.orEmpty())
                }
                append(" | ")
                append(movie.year.toString())
            }
        )

        // Длина фильма и возрастное ограничение
        Text(
            modifier = Modifier.padding(8.dp),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Длительность: ")
                }
                append(movie.movieLength.toString())
                append(" мин | ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Возрастные ограничения: ")
                }
                append(movie.ageRating.toString())
            }
        )

        // Полное описание фильма
        Text(
            modifier = Modifier.padding(8.dp),
            text = movie.description.orEmpty()
        )

        // Рейтинг и голоса
        LazyRow(modifier = Modifier.padding(top = 8.dp)) {
        }
    }
}
