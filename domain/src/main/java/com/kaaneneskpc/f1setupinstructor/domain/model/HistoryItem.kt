package com.kaaneneskpc.f1setupinstructor.domain.model

import java.time.Instant

data class HistoryItem(
    val timestamp: Instant,
    val circuit: String,
    val weatherQuali: String,
    val weatherRace: String,
    val selectedSetupId: String?,
    val isFavorite: Boolean
)
