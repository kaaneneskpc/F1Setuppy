package com.kaaneneskpc.f1setupinstructor.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "history")
data class HistoryItemEntity(
    @PrimaryKey
    val timestamp: Instant,
    val circuit: String,
    val weatherQuali: String,
    val weatherRace: String,
    val selectedSetupId: String?,
    val isFavorite: Boolean
)
