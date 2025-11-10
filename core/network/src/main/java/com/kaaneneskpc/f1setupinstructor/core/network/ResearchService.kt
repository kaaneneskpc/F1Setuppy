package com.kaaneneskpc.f1setupinstructor.core.network

import com.kaaneneskpc.f1setupinstructor.domain.model.SetupData

interface ResearchService {
    suspend fun getSetupFromAi(
        track: String,
        sessionType: String,
        qualyWeather: String,
        raceWeather: String
    ): Result<SetupData>
}
