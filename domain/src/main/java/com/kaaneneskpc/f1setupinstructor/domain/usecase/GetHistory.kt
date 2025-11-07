package com.kaaneneskpc.f1setupinstructor.domain.usecase

import com.kaaneneskpc.f1setupinstructor.domain.model.HistoryItem
import kotlinx.coroutines.flow.Flow

interface GetHistory {
    operator fun invoke(): Flow<List<HistoryItem>>
}
