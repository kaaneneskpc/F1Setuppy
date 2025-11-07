package com.kaaneneskpc.f1setupinstructor.di

import com.kaaneneskpc.f1setupinstructor.domain.usecase.AskChatbot
import com.kaaneneskpc.f1setupinstructor.domain.usecase.AskChatbotImpl
import com.kaaneneskpc.f1setupinstructor.domain.usecase.GetHistory
import com.kaaneneskpc.f1setupinstructor.domain.usecase.GetHistoryImpl
import com.kaaneneskpc.f1setupinstructor.domain.usecase.GetSetups
import com.kaaneneskpc.f1setupinstructor.domain.usecase.GetSetupsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindGetSetupsUseCase(getSetupsImpl: GetSetupsImpl): GetSetups

    @Binds
    abstract fun bindGetHistoryUseCase(getHistoryImpl: GetHistoryImpl): GetHistory

    @Binds
    abstract fun bindAskChatbotUseCase(askChatbotImpl: AskChatbotImpl): AskChatbot
}
