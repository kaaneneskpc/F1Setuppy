package com.kaaneneskpc.f1setupinstructor.domain.usecase

import androidx.paging.PagingData
import com.kaaneneskpc.f1setupinstructor.domain.model.Setup
import com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle
import kotlinx.coroutines.flow.Flow

interface GetSetups {
    operator fun invoke(circuit: String, qualiWeather: String, raceWeather: String, style: SetupStyle?): Flow<PagingData<Setup>>
}
