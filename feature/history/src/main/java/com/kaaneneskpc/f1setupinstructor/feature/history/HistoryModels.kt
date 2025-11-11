package com.kaaneneskpc.f1setupinstructor.feature.history

import androidx.compose.runtime.Stable

@Stable
data class HistoryUiState(
    val query: String = "",
    val sort: SortOption = SortOption.Recent,
    val sections: List<HistorySection> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val error: String? = null
)

enum class SortOption {
    Recent,
    Oldest,
    AtoZ,
    ZtoA
}

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

enum class WeatherIcon {
    Sunny,
    Cloudy,
    Rainy
}

