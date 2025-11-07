package com.kaaneneskpc.f1setupinstructor.domain.usecase

import com.kaaneneskpc.f1setupinstructor.domain.model.HistoryItem
import com.kaaneneskpc.f1setupinstructor.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistoryImpl @Inject constructor(
    private val historyRepository: HistoryRepository
) : GetHistory {
    override fun invoke(): Flow<List<HistoryItem>> = historyRepository.getHistory()
}
