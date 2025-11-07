package com.kaaneneskpc.f1setupinstructor.domain.usecase

import androidx.paging.PagingData
import com.kaaneneskpc.f1setupinstructor.domain.model.Setup
import com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle
import com.kaaneneskpc.f1setupinstructor.domain.repository.SetupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSetupsImpl @Inject constructor(
    private val setupRepository: SetupRepository
) : GetSetups {
    override fun invoke(circuit: String, qualiWeather: String, raceWeather: String, style: SetupStyle?): Flow<PagingData<Setup>> {
        return setupRepository.getSetups(circuit, qualiWeather, raceWeather, style)
    }
}
