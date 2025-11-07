package com.kaaneneskpc.f1setupinstructor.domain.repository

import com.kaaneneskpc.f1setupinstructor.domain.model.HistoryItem
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistory(): Flow<List<HistoryItem>>
    suspend fun saveHistoryItem(historyItem: HistoryItem)
}
