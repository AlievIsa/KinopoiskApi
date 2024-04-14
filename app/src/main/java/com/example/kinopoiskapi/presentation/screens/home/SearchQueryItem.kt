package com.example.kinopoiskapi.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kinopoiskapi.domain.SearchQuery
import kotlinx.coroutines.Job

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchQueryItem(
    searchQuery: SearchQuery,
    onSearchTextChange: (text: String) -> Unit,
    onSearchClicked: () -> Unit,
    onDeleteSearchQuery: (query: SearchQuery) -> Job
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDeleteSearchQuery(searchQuery)
                true
            } else
                false
        }
    )
    LaunchedEffect(searchQuery) {
        dismissState.reset()
    }
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onSearchTextChange(searchQuery.text)
                    onSearchClicked()
                }
                .padding(8.dp)
        ) {
            Icon(
                modifier = Modifier.padding(end = 8.dp),
                imageVector = Icons.Default.History,
                contentDescription = "History Icon"
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                text = searchQuery.text, maxLines = 1
            )
        }
    }
}