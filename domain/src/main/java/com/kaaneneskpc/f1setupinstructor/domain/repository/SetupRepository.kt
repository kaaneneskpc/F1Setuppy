package com.kaaneneskpc.f1setupinstructor.domain.repository

import androidx.paging.PagingData
import com.kaaneneskpc.f1setupinstructor.domain.model.Setup
import com.kaaneneskpc.f1setupinstructor.domain.model.SetupData
import com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle
import kotlinx.coroutines.flow.Flow

interface SetupRepository {
    /**
     * Get paginated list of setups from cache
     */
    fun getSetups(circuit: String, qualiWeather: String, raceWeather: String, style: SetupStyle?): Flow<PagingData<Setup>>
    
    /**
     * Get best setup from AI (used by HomeScreen)
     */
    suspend fun getBestSetup(track: String, qualyWeather: String, raceWeather: String): Result<SetupData>
    
    /**
     * Get setup detail by source URL
     */
    suspend fun getSetupDetail(sourceUrl: String): Setup
    
    /**
     * Save setup as favorite
     */
    suspend fun saveFavorite(setup: Setup)
}
