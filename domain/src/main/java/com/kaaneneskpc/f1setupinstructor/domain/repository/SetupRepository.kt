package com.kaaneneskpc.f1setupinstructor.domain.repository

import androidx.paging.PagingData
import com.kaaneneskpc.f1setupinstructor.domain.model.Setup
import com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle
import kotlinx.coroutines.flow.Flow

interface SetupRepository {
    fun getSetups(circuit: String, qualiWeather: String, raceWeather: String, style: SetupStyle?): Flow<PagingData<Setup>>
    suspend fun getSetupDetail(sourceUrl: String): Setup
    suspend fun saveFavorite(setup: Setup)
}
