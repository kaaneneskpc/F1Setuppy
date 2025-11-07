package com.kaaneneskpc.f1setupinstructor.core.data.repository

import com.kaaneneskpc.f1setupinstructor.core.data.mapper.toDomainModel
import com.kaaneneskpc.f1setupinstructor.core.data.mapper.toEntity
import com.kaaneneskpc.f1setupinstructor.core.database.dao.HistoryDao
import com.kaaneneskpc.f1setupinstructor.domain.model.HistoryItem
import com.kaaneneskpc.f1setupinstructor.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override fun getHistory(): Flow<List<HistoryItem>> {
        return historyDao.getHistory().map {
            it.map { historyItemEntity ->
                historyItemEntity.toDomainModel()
            }
        }
    }

    override suspend fun saveHistoryItem(historyItem: HistoryItem) {
        historyDao.insert(historyItem.toEntity())
    }
}
