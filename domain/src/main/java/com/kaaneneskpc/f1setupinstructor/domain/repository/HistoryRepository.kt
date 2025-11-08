package com.kaaneneskpc.f1setupinstructor.domain.repository

import com.kaaneneskpc.f1setupinstructor.domain.model.HistoryItem
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface HistoryRepository {
    fun getHistories(): Flow<List<HistoryItem>>
    suspend fun getHistoryByTimestamp(timestamp: Instant): HistoryItem?
    suspend fun insertHistory(history: HistoryItem)
    suspend fun deleteHistory(history: HistoryItem)
}
