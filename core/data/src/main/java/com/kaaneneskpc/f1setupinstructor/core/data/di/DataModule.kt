package com.kaaneneskpc.f1setupinstructor.core.data.di

import com.kaaneneskpc.f1setupinstructor.core.data.repository.HistoryRepositoryImpl
import com.kaaneneskpc.f1setupinstructor.core.data.repository.SetupRepositoryImpl
import com.kaaneneskpc.f1setupinstructor.domain.repository.HistoryRepository
import com.kaaneneskpc.f1setupinstructor.domain.repository.SetupRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindSetupRepository(
        setupRepositoryImpl: SetupRepositoryImpl
    ): SetupRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl
    ): HistoryRepository
}
