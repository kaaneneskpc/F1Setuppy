package com.kaaneneskpc.f1setupinstructor.feature.history

sealed interface HistoryEvent {
    data object OnBack : HistoryEvent
    data object OnNewSetup : HistoryEvent
    data class OnQueryChange(val q: String) : HistoryEvent
    data object OnSortClick : HistoryEvent
    data class OnSortSelect(val option: SortOption) : HistoryEvent
    data class OnItemClick(val id: String) : HistoryEvent
    data object OnClearQuery : HistoryEvent
}

