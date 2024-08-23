package com.example.kinopoiskapi.presentation.screens.movie

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kinopoiskapi.domain.Movie
import com.example.kinopoiskapi.presentation.ui.theme.GrayContainer
import com.example.kinopoiskapi.presentation.utils.SetStatusBarColor
import kotlinx.coroutines.Job
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    navController: NavController,
    movieViewModel: MovieViewModel = hiltViewModel()
) {

    SetStatusBarColor(color = Color.Transparent)
    val movie by movieViewModel.movie.collectAsState()

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )
    var isBottomSheetExpanded by remember {
        mutableStateOf(true)
    }
    val seasonsNum by movieViewModel.seasonsNum.collectAsState()


    LaunchedEffect(bottomSheetState.currentValue) {
        isBottomSheetExpanded = when (bottomSheetState.currentValue) {
            SheetValue.Expanded -> true
            else -> false
        }
        Log.d("BottomSheet", "${bottomSheetState.currentValue}")
    }

    if (isBottomSheetExpanded)
        SetStatusBarColor(color = MaterialTheme.colorScheme.primary)
    else
        SetStatusBarColor(color = Color.Transparent)


    BottomSheetScaffold(
        sheetContent = {
            Content(movie, seasonsNum, movieViewModel::getSeasonsNum)
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 480.dp,
        sheetShape = BottomSheetDefaults.HiddenShape,
        sheetDragHandle = {},
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(color = Color.Black)
                .fillMaxSize()
        ) {
            movie.backdrop?.url?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Кадр",
                    modifier = Modifier.align(Alignment.TopCenter),
                    contentScale = ContentScale.Crop
                )
            }
            if (movie.backdrop?.url == null) {
                movie.poster?.url?.let { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Постер",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        fontSize = 16.sp,
                        maxLines = 1,
                        text = movie.name ?: "<Без названия>",
                        fontWeight = FontWeight.Bold
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = if (isBottomSheetExpanded) LocalContentColor.current else Color.White
                        )
                    }
                },
                colors = if (isBottomSheetExpanded) {
                    TopAppBarDefaults.topAppBarColors()
                } else {
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(movie: Movie, seasonsNum: Int?, getSeasonsNum: (Int) -> Job) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.88f)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        var isDescriptionExpanded by remember {
            mutableStateOf(false)
        }

//        // Лого
//        movie.logo?.url?.let { imageUrl ->
//            AsyncImage(
//                model = imageUrl,
//                contentDescription = "Movie Poster",
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .padding(bottom = 16.dp)
//                    .width(196.dp)
//            )
//        }

        // Страна, жанр и год
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(movie.country.orEmpty())
                }
                append(" | ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(movie.firstGenre.orEmpty())
                }
                append(" | ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(movie.secondGenre.orEmpty())
                }
                append(" | ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(movie.year.toString())
                }
            }
        )

        // Dозрастное ограничение и длина фильма или количество сезонов
        movie.ageRating?.let {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "Возрастное ограничение: ${movie.ageRating.toString()}+"
            )
        }

        if (movie.movieLength != null) {
            Text(text = "Длительность: ${movie.movieLength} мин")
        } else {
            movie.isSeries?.let { isSeries ->
                if (isSeries) {
                    getSeasonsNum(movie.dtoId)
                    Text(text = "Сезонов: ${seasonsNum ?: 0}")
                }
            }
        }

        // Рейтинг и голоса
        LazyRow(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            movie.rating?.kp?.let { rating ->
                item {
                    RatingVotesItem(rating, movie.votes?.kp, "Кинопоиска")
                }
            }
            movie.rating?.imdb?.let { rating ->
                item {
                    RatingVotesItem(rating, movie.votes?.imdb, "IMDb")
                }
            }
            movie.rating?.await?.let { rating ->
                item {
                    RatingVotesItem(rating, movie.votes?.await, "Await")
                }
            }
            movie.rating?.filmCritics?.let { rating ->
                item {
                    RatingVotesItem(rating, movie.votes?.filmCritics, "FilmCritics")
                }
            }
            movie.rating?.russianFilmCritics?.let { rating ->
                item {
                    RatingVotesItem(rating, movie.votes?.russianFilmCritics, "RussianFilmCritics")
                }
            }
        }

        // Полное описание фильма
        movie.description?.let { description ->
            if (description.isNotEmpty()) {
                Column(modifier = Modifier.padding(vertical = 12.dp)) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "Описание",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = description,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 3
                    )

                    TextButton(
                        onClick = { isDescriptionExpanded = !isDescriptionExpanded },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = if (isDescriptionExpanded) "Свернуть" else "Развернуть")
                    }
                }
            }
        }
    }
}

@Composable
fun RatingVotesItem(rating: Double, votes: Int?, name: String) {
    Card(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = GrayContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                text = String.format(Locale.US, "%.1f", rating)
            )
            Column(
                modifier = Modifier.padding(start = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Рейтинг $name",
                    fontSize = 14.sp
                )
                votes?.let { votes ->
                    val formattedVotes = NumberFormat.getInstance(Locale.US).format(votes)
                    Text(
                        text = "$formattedVotes оценок",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
