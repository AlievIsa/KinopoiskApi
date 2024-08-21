package com.example.kinopoiskapi.presentation.screens.home.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.kinopoiskapi.domain.Movie
import com.example.kinopoiskapi.presentation.ui.theme.Green

@Composable
fun SearchMovieItem(
    movie: Movie
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Box(modifier = Modifier.width(40.dp).height(50.dp)) {
                movie.poster?.url?.let { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Movie Poster",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Column(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Row {
                    Text(
                        text = movie.name ?: "<Без названия>",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .weight(1f)
                    )
                    movie.rating?.kp?.let { rating ->
                        Text(
                            text = "%.1f".format(rating),
                            color = Green,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        text = movie.country.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = movie.genre.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    movie.year?.let { year ->
                        Text(
                            text = year.toString(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}