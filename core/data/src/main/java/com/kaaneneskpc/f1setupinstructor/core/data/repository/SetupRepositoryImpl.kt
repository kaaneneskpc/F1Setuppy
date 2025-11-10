package com.kaaneneskpc.f1setupinstructor.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kaaneneskpc.f1setupinstructor.core.data.mapper.toDomainModel
import com.kaaneneskpc.f1setupinstructor.core.data.mapper.toDomainSetup
import com.kaaneneskpc.f1setupinstructor.core.data.mapper.toEntity
import com.kaaneneskpc.f1setupinstructor.core.database.dao.SetupDao
import com.kaaneneskpc.f1setupinstructor.core.network.ResearchService
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
    private val researchService: ResearchService,
    private val externalScope: CoroutineScope
) : SetupRepository {

    override fun getSetups(
        circuit: String,
        qualiWeather: String,
        raceWeather: String,
        style: SetupStyle?
    ): Flow<PagingData<Setup>> {
        // AI'dan setup al ve cache'e kaydet (background'da)
        externalScope.launch {
            try {
                // Varsayılan olarak Race setup'ı al
                val result = researchService.getSetupFromAi(circuit, "Race", qualiWeather, raceWeather)
                result.onSuccess { setupData ->
                    // SetupData'yı Setup domain modeline çevir ve database'e kaydet
                    val setup = setupData.toDomainSetup()
                    setupDao.insert(setup.toEntity())
                }
            } catch (e: Exception) {
                // AI fetch failed, just use cached data
                // Log error but don't crash
                e.printStackTrace()
            }
        }

        // Cache'den döndür
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

    override suspend fun getBestSetup(
        track: String,
        sessionType: String,
        qualyWeather: String,
        raceWeather: String
    ): Result<com.kaaneneskpc.f1setupinstructor.domain.model.SetupData> {
        return researchService.getSetupFromAi(track, sessionType, qualyWeather, raceWeather)
    }

    override suspend fun saveFavorite(setup: Setup) {
        // Save setup to database (marking as favorite can be added later)
        setupDao.insert(setup.toEntity())
    }
}
