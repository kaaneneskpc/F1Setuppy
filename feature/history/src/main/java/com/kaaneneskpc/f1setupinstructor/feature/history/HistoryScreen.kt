package com.kaaneneskpc.f1setupinstructor.feature.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.kaaneneskpc.f1setupinstructor.core.ui.R
import com.kaaneneskpc.f1setupinstructor.core.ui.components.GradientBackground

// State models with error handling
data class HistoryUiState(
    val query: String = "",
    val sort: SortOption = SortOption.Recent,
    val sections: List<HistorySection> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val error: String? = null,
    // Bottom sheet states can be added here
)

enum class SortOption { Recent, Oldest, AtoZ, ZtoA }

data class HistorySection(
    val title: String,
    val items: List<HistoryItem>
)

data class HistoryItem(
    val id: String,
    val flagUrl: String?,
    val title: String,
    val subtitle: String,
    val weather: WeatherIcon
)

enum class WeatherIcon { Sunny, Cloudy, Rainy }

// Events
sealed interface HistoryEvent {
    data object OnBack : HistoryEvent
    data object OnNewSetup : HistoryEvent
    data class OnQueryChange(val q: String) : HistoryEvent
    data object OnSortClick : HistoryEvent
    data class OnSortSelect(val option: SortOption) : HistoryEvent
    data class OnItemClick(val id: String) : HistoryEvent
    data object OnClearQuery : HistoryEvent
    // Other filter events can be added here
}

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
        // Title
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

        // Search Field
        item {
            SearchField(
                query = uiState.query,
                onQueryChange = { onEvent(HistoryEvent.OnQueryChange(it)) },
                onClear = { onEvent(HistoryEvent.OnClearQuery) }
            )
        }

        // Filter Bar
        item {
            FilterBar(
                onSortClick = { onEvent(HistoryEvent.OnSortClick) },
                onCircuitClick = { /* TODO */ },
                onCarClick = { /* TODO */ }
            )
        }

        // Loading / Empty / Error states
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
                // History List (normal state)
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
                
                // Bottom padding for last item
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// Composables for different states
@Composable
private fun EmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Henüz setup geçmişin yok",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Yeni bir setup oluşturarak başla!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = message,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}

// Other Composables (TopBar, SearchField, etc.) remain largely the same
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryTopBar(onBack: () -> Unit, onNewSetup: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text("Setup Geçmişim", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
        actions = {
            TextButton(onClick = onNewSetup) {
                Text("Yeni", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Composable
fun SearchField(query: String, onQueryChange: (String) -> Unit, onClear: () -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Pist veya araç ara…") },
        leadingIcon = { Icon(Icons.Default.Search, "Search") },
        trailingIcon = { if (query.isNotEmpty()) IconButton(onClick = onClear) { Icon(Icons.Default.Clear, "Clear") } },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.Red,
            focusedBorderColor = Color.Red,
            unfocusedBorderColor = Color.Gray,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.Gray,
            focusedTrailingIconColor = Color.White,
            unfocusedTrailingIconColor = Color.Gray,
            focusedPlaceholderColor = Color.Gray,
            unfocusedPlaceholderColor = Color.Gray
        )
    )
}

@Composable
fun FilterBar(onSortClick: () -> Unit, onCircuitClick: () -> Unit, onCarClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = false, 
            onClick = onSortClick, 
            label = { Text("Sırala", color = Color.White) }, 
            leadingIcon = { Icon(Icons.Default.CalendarToday, "Sort", tint = Color.White) },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color.DarkGray.copy(alpha = 0.3f),
                labelColor = Color.White,
                iconColor = Color.White
            )
        )
        FilterChip(
            selected = false, 
            onClick = onCircuitClick, 
            label = { Text("Pist", color = Color.White) }, 
            leadingIcon = { Icon(Icons.Default.LocationOn, "Track", tint = Color.White) },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color.DarkGray.copy(alpha = 0.3f),
                labelColor = Color.White,
                iconColor = Color.White
            )
        )
        FilterChip(
            selected = false, 
            onClick = onCarClick, 
            label = { Text("Araç", color = Color.White) }, 
            leadingIcon = { Icon(Icons.Default.DirectionsCar, "Car", tint = Color.White) },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color.DarkGray.copy(alpha = 0.3f),
                labelColor = Color.White,
                iconColor = Color.White
            )
        )
    }
}

@Composable
fun HistoryList(sections: List<HistorySection>, onItemClick: (String) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        sections.forEach { section ->
            item {
                HistorySectionHeader(title = section.title)
            }
            items(section.items) { item ->
                HistoryRow(item = item, onClick = { onItemClick(item.id) })
            }
        }
    }
}

@Composable
fun HistorySectionHeader(title: String) {
    Text(
        text = title, 
        style = MaterialTheme.typography.titleLarge, 
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun HistoryRow(item: HistoryItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.flagUrl,
            contentDescription = item.title,
            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_placeholder),
            error = painterResource(id = R.drawable.ic_placeholder)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title, 
                style = MaterialTheme.typography.titleMedium, 
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = item.subtitle, 
                style = MaterialTheme.typography.bodyMedium, 
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        val weatherIcon = when (item.weather) {
            WeatherIcon.Sunny -> Icons.Default.WbSunny
            WeatherIcon.Cloudy -> Icons.Default.Cloud
            WeatherIcon.Rainy -> painterResource(id = R.drawable.ic_rain)
        }
        Icon(
            painter = if (weatherIcon is androidx.compose.ui.graphics.vector.ImageVector) androidx.compose.ui.graphics.vector.rememberVectorPainter(weatherIcon) else weatherIcon as androidx.compose.ui.graphics.painter.Painter,
            contentDescription = item.weather.name,
            tint = Color.White
        )
    }
}

// Previews
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
