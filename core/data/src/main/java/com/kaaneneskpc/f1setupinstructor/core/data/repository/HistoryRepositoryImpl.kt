package com.kaaneneskpc.f1setupinstructor.core.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.kaaneneskpc.f1setupinstructor.core.data.mapper.toDomainModel
import com.kaaneneskpc.f1setupinstructor.core.data.mapper.toEntity
import com.kaaneneskpc.f1setupinstructor.core.database.dao.HistoryDao
import com.kaaneneskpc.f1setupinstructor.domain.model.HistoryItem
import com.kaaneneskpc.f1setupinstructor.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override fun getHistories(): Flow<List<HistoryItem>> {
        return historyDao.getHistories().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getHistoryByTimestamp(timestamp: Instant): HistoryItem? {
        return historyDao.getHistoryByTimestamp(timestamp.toEpochMilli())?.toDomainModel()
    }

    override suspend fun insertHistory(history: HistoryItem) {
        historyDao.insertHistory(history.toEntity())
    }

    override suspend fun deleteHistory(history: HistoryItem) {
        historyDao.deleteHistory(history.toEntity())
    }
}
