package com.kaaneneskpc.f1setupinstructor.core.data.mapper

import com.kaaneneskpc.f1setupinstructor.core.database.entity.HistoryItemEntity
import com.kaaneneskpc.f1setupinstructor.domain.model.HistoryItem

fun HistoryItemEntity.toDomainModel(): HistoryItem {
    return HistoryItem(
        timestamp = timestamp,
        circuit = circuit,
        weatherQuali = weatherQuali,
        weatherRace = weatherRace,
        selectedSetupId = selectedSetupId,
        isFavorite = isFavorite
    )
}

fun HistoryItem.toEntity(): HistoryItemEntity {
    return HistoryItemEntity(
        timestamp = timestamp,
        circuit = circuit,
        weatherQuali = weatherQuali,
        weatherRace = weatherRace,
        selectedSetupId = selectedSetupId,
        isFavorite = isFavorite
    )
}
