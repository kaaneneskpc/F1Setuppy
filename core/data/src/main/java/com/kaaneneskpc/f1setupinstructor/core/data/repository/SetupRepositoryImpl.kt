package com.kaaneneskpc.f1setupinstructor.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kaaneneskpc.f1setupinstructor.core.data.mapper.toDomainModel
import com.kaaneneskpc.f1setupinstructor.core.data.mapper.toEntity
import com.kaaneneskpc.f1setupinstructor.core.database.dao.SetupDao
import com.kaaneneskpc.f1setupinstructor.core.network.FakeResearchService
import com.kaaneneskpc.f1setupinstructor.domain.model.Setup
import com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle
import com.kaaneneskpc.f1setupinstructor.domain.repository.SetupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SetupRepositoryImpl @Inject constructor(
    private val setupDao: SetupDao,
    private val researchService: FakeResearchService,
    private val externalScope: CoroutineScope
) : SetupRepository {

    override fun getSetups(
        circuit: String,
        qualiWeather: String,
        raceWeather: String,
        style: SetupStyle?
    ): Flow<PagingData<Setup>> {
        externalScope.launch {
            val setups = researchService.getSetups(circuit, qualiWeather, raceWeather, style)
            setupDao.insertAll(setups.map { it.toEntity() })
        }

        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { setupDao.getSetups(circuit, qualiWeather, raceWeather) }
        ).flow.map {
            it.map { setupEntity ->
                setupEntity.toDomainModel()
            }
        }
    }

    override suspend fun getSetupDetail(sourceUrl: String): Setup {
        return setupDao.getSetupBySourceUrl(sourceUrl)!!.toDomainModel()
    }

    override suspend fun saveFavorite(setup: Setup) {
        // TODO: Implement this
    }
}
