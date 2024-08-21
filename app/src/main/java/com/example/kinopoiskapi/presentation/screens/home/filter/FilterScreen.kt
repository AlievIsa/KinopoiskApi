package com.example.kinopoiskapi.presentation.screens.home.filter

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SliderState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kinopoiskapi.R
import com.example.kinopoiskapi.presentation.navigation.Screen
import com.example.kinopoiskapi.presentation.screens.home.HomeViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(navController.getBackStackEntry(Screen.Home.route))
) {
    val context = LocalContext.current

    val typeOptions = mutableListOf(
        stringResource(R.string.any),
        stringResource(R.string.movie),
        stringResource(R.string.serial)
    )
    var selectedTypeIndex by remember {
        mutableIntStateOf(homeViewModel.type.value)
    }

    var rateSliderPosition by remember {
        mutableStateOf(homeViewModel.rateRange.value)
    }

    var selectedYear by remember {
        mutableStateOf(homeViewModel.selectedYear.value)
    }

    var selectedGenre by remember {
        mutableStateOf(homeViewModel.selectedGenre.value)
    }

    val lastYear = Calendar.getInstance().get(Calendar.YEAR) + 2
    val yearList = (lastYear downTo 1911).toList().map { it.toString() }
    val genres by homeViewModel.genres.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.search_with_filters))
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_icon)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            LaunchedEffect(key1 = genres) {
                if (genres.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Need internet connection to download genres",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    typeOptions.forEachIndexed { index, option ->
                        SegmentedButton(
                            selected = selectedTypeIndex == index,
                            onClick = { selectedTypeIndex = index },
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = typeOptions.size
                            )
                        ) {
                            Text(text = option)
                        }
                    }
                }

                ChipComponent(
                    selected = selectedGenre ?: stringResource(R.string.any_genre),
                    list = genres,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    selectedGenre = it
                }

                ChipComponent(
                    selected = selectedYear ?: stringResource(R.string.any_year),
                    list = yearList,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    selectedYear = it
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    val floatRange =
                        rateSliderPosition.first.toFloat()..rateSliderPosition.last.toFloat()
                    RangeSlider(
                        value = floatRange,
                        steps = 9,
                        onValueChange = { range ->
                            rateSliderPosition = range.start.toInt()..range.endInclusive.toInt()
                        },
                        valueRange = 0f..10f,
                        onValueChangeFinished = {}
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {

                        Text(
                            modifier = Modifier.align(Alignment.CenterStart),
                            text = stringResource(R.string.rating)
                        )

                        val start = rateSliderPosition.start.toInt()
                        val end = rateSliderPosition.endInclusive.toInt()
                        Text(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            text = when {
                                start == 0 && end == 10 ->
                                    stringResource(id = R.string.no_matter)
                                start == end -> start.toString()
                                else -> stringResource(
                                    R.string.rating_from_to,
                                    start,
                                    end
                                )
                            }
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    onClick = {
                        selectedTypeIndex = 0
                        selectedYear = null
                        selectedGenre = null
                        rateSliderPosition = 0..10
                    }
                ) {
                    Text(text = stringResource(R.string.refresh_filters))
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    onClick = {
                        homeViewModel.updateFilters(
                            selectedTypeIndex,
                            typeOptions[selectedTypeIndex],
                            selectedGenre,
                            selectedYear,
                            rateSliderPosition
                        )
                        navController.popBackStack()
                    }
                ) {
                    Text(text = stringResource(R.string.search))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipComponent(selected: String, list: List<String>, modifier: Modifier, onSelectedChange: (String) -> Unit) {

    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        SuggestionChip(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            onClick = {  },
            label = { Text(text = selected) },
            icon = {
                Icon(
                    imageVector = if (!expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = "Icon down/up"
                )
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize()
        ) {
            list.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        onSelectedChange(item)
                        expanded = false
                    }
                )
            }
        }
    }

}
