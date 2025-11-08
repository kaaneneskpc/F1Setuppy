package com.kaaneneskpc.f1setupinstructor.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.f1setupinstructor.domain.model.HistoryItem
import com.kaaneneskpc.f1setupinstructor.domain.usecase.GetHistoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoriesUseCase: GetHistoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private var histories: List<HistoryItem> = emptyList()

    init {
        loadHistories()
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.OnQueryChange -> _uiState.update { it.copy(query = event.q) }
            HistoryEvent.OnClearQuery -> _uiState.update { it.copy(query = "") }
            is HistoryEvent.OnSortSelect -> {
                _uiState.update { it.copy(sort = event.option) }
                sortAndGroupHistories()
            }
            else -> Unit // Other events like filter clicks can be handled here
        }
    }

    private fun loadHistories() {
        getHistoriesUseCase()
            .onEach { fetchedHistories ->
                this.histories = fetchedHistories
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEmpty = fetchedHistories.isEmpty(),
                        sections = groupHistoriesByDate(fetchedHistories)
                    )
                }
            }
            .catch { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEmpty = true,
                        error = throwable.localizedMessage ?: "Failed to load history"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun sortAndGroupHistories() {
        val sortedHistories = when (_uiState.value.sort) {
            SortOption.Recent -> histories.sortedByDescending { it.timestamp }
            SortOption.Oldest -> histories.sortedBy { it.timestamp }
            SortOption.AtoZ -> histories.sortedBy { it.circuit }
            SortOption.ZtoA -> histories.sortedByDescending { it.circuit }
        }
        _uiState.update { it.copy(sections = groupHistoriesByDate(sortedHistories)) }
    }
    
    private fun groupHistoriesByDate(histories: List<HistoryItem>): List<HistorySection> {
        if (histories.isEmpty()) return emptyList()
        // A simple grouping, can be made more complex (e.g., "This week", "Last month")
        return listOf(HistorySection("Recent Setups", histories.map { it.toUiHistoryItem() }))
    }

    private fun HistoryItem.toUiHistoryItem(): com.kaaneneskpc.f1setupinstructor.feature.history.HistoryItem {
        val dateString = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date(timestamp.toEpochMilli()))
        return com.kaaneneskpc.f1setupinstructor.feature.history.HistoryItem(
            id = timestamp.toEpochMilli().toString(),
            flagUrl = getFlagUrlForTrack(circuit),
            title = circuit,
            subtitle = "$weatherQuali → $weatherRace | $dateString",
            weather = when {
                weatherRace.contains("Dry", ignoreCase = true) -> WeatherIcon.Sunny
                weatherRace.contains("Cloudy", ignoreCase = true) -> WeatherIcon.Cloudy
                weatherRace.contains("Wet", ignoreCase = true) -> WeatherIcon.Rainy
                else -> WeatherIcon.Sunny
            }
        )
    }

    private fun getFlagUrlForTrack(track: String): String {
        // This is a placeholder. In a real app, this mapping would be more robust.
        return when {
            track.contains("Monza", ignoreCase = true) -> "https://flagcdn.com/w320/it.png"
            track.contains("Silverstone", ignoreCase = true) -> "https://flagcdn.com/w320/gb.png"
            track.contains("Spa", ignoreCase = true) -> "https://flagcdn.com/w320/be.png"
            track.contains("Suzuka", ignoreCase = true) -> "https://flagcdn.com/w320/jp.png"
            else -> "https://flagcdn.com/w320/ua.png" // Placeholder flag
        }
    }
}
