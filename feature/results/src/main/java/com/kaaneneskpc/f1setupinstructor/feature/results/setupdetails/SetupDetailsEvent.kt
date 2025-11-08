package com.kaaneneskpc.f1setupinstructor.feature.results.setupdetails

sealed interface SetupDetailsEvent {
    data class TabSelected(val index: Int) : SetupDetailsEvent
    object ToggleFavorite : SetupDetailsEvent
    object BackClicked : SetupDetailsEvent
    object ShareClicked : SetupDetailsEvent
}
