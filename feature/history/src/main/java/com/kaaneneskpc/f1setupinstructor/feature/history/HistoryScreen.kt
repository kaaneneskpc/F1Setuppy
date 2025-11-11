package com.kaaneneskpc.f1setupinstructor.feature.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaaneneskpc.f1setupinstructor.core.ui.components.GradientBackground
import com.kaaneneskpc.f1setupinstructor.feature.history.components.EmptyState
import com.kaaneneskpc.f1setupinstructor.feature.history.components.ErrorState
import com.kaaneneskpc.f1setupinstructor.feature.history.components.FilterBar
import com.kaaneneskpc.f1setupinstructor.feature.history.components.HistoryRow
import com.kaaneneskpc.f1setupinstructor.feature.history.components.HistorySectionHeader
import com.kaaneneskpc.f1setupinstructor.feature.history.components.SearchField

@Composable
fun HistoryRoute(
    onBack: () -> Unit,
    onNewSetup: () -> Unit,
    onNavigateToSetup: (String) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GradientBackground {
        HistoryScreen(
            uiState = uiState,
            onEvent = { event ->
                when (event) {
                    HistoryEvent.OnBack -> onBack()
                    HistoryEvent.OnNewSetup -> onNewSetup()
                    is HistoryEvent.OnItemClick -> onNavigateToSetup(event.id)
                    else -> viewModel.onEvent(event)
                }
            }
        )
    }
}

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    onEvent: (HistoryEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Setup Geçmişim",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }

        item {
            SearchField(
                query = uiState.query,
                onQueryChange = { onEvent(HistoryEvent.OnQueryChange(it)) },
                onClear = { onEvent(HistoryEvent.OnClearQuery) }
            )
        }

        item {
            FilterBar(
                onSortClick = { onEvent(HistoryEvent.OnSortClick) },
                onCircuitClick = { /* TODO */ },
                onCarClick = { /* TODO */ }
            )
        }

        when {
            uiState.isLoading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Red)
                    }
                }
            }
            uiState.error != null -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        ErrorState(message = uiState.error)
                    }
                }
            }
            uiState.isEmpty -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        EmptyState()
                    }
                }
            }
            else -> {
                uiState.sections.forEach { section ->
                    item {
                        HistorySectionHeader(title = section.title)
                    }
                    items(section.items.size) { index ->
                        val item = section.items[index]
                        HistoryRow(
                            item = item,
                            onClick = { onEvent(HistoryEvent.OnItemClick(item.id)) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

val sampleSections = listOf(
    HistorySection("This Week", listOf(
        HistoryItem("1", "https://flagcdn.com/w320/it.png", "Monza Grand Prix", "Mercedes-AMG W14 | 2 days ago", WeatherIcon.Sunny),
        HistoryItem("2", "https://flagcdn.com/w320/gb.png", "Silverstone Circuit", "Red Bull Racing RB19 | 4 days ago", WeatherIcon.Cloudy)
    )),
    HistorySection("Last Week", listOf(
        HistoryItem("3", "https://flagcdn.com/w320/be.png", "Spa-Francorchamps", "Ferrari SF-23 | 15.10.2023", WeatherIcon.Rainy)
    ))
)

@Preview(showBackground = true, backgroundColor = 0xFF1B0F0F)
@Composable
fun HistoryScreenPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        HistoryScreen(uiState = HistoryUiState(sections = sampleSections), onEvent = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1B0F0F)
@Composable
fun HistoryScreenEmptyPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        HistoryScreen(uiState = HistoryUiState(isEmpty = true), onEvent = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1B0F0F)
@Composable
fun HistoryScreenLoadingPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        HistoryScreen(uiState = HistoryUiState(isLoading = true), onEvent = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1B0F0F)
@Composable
fun HistoryScreenErrorPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        HistoryScreen(uiState = HistoryUiState(error = "Failed to load setup history."), onEvent = {})
    }
}
