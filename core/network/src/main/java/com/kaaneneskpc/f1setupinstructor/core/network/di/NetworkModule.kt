package com.kaaneneskpc.f1setupinstructor.core.network.di

import com.kaaneneskpc.f1setupinstructor.core.network.FakeResearchService
import com.kaaneneskpc.f1setupinstructor.core.network.ResearchService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    abstract fun bindResearchService(researchServiceImpl: FakeResearchService): ResearchService
}
