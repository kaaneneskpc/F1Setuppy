package com.kaaneneskpc.f1setupinstructor.core.network.di

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.kaaneneskpc.f1setupinstructor.core.network.ChatService
import com.kaaneneskpc.f1setupinstructor.core.network.ChatServiceImpl
import com.kaaneneskpc.f1setupinstructor.core.network.ResearchService
import com.kaaneneskpc.f1setupinstructor.core.network.ResearchServiceImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides Moshi JSON parser
     */
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    /**
     * Provides Google Gemini AI Generative Model
     * API key is read from local.properties for security
     */
    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        // API key is read from local.properties via BuildConfig
        val apiKey = com.kaaneneskpc.f1setupinstructor.core.network.BuildConfig.GEMINI_API_KEY
        
        if (apiKey.isEmpty()) {
            throw IllegalStateException(
                "Gemini API key not configured! " +
                "Please add GEMINI_API_KEY=your_key to local.properties"
            )
        }
        
        return GenerativeModel(
            modelName = "gemini-2.5-flash", // Fast model with internet search capability
            apiKey = apiKey,
            generationConfig = generationConfig {
                temperature = 0.7f // Balance between creativity and consistency
                topK = 40
                topP = 0.95f
                maxOutputTokens = 8192 // Increased for detailed setup JSON
            }
        )
    }

    /**
     * Provides ResearchService implementation using Gemini AI
     */
    @Provides
    @Singleton
    fun provideResearchService(
        generativeModel: GenerativeModel,
        moshi: Moshi
    ): ResearchService {
        return ResearchServiceImpl(generativeModel, moshi)
    }

    /**
     * Provides ChatService implementation using Gemini AI
     */
    @Provides
    @Singleton
    fun provideChatService(
        generativeModel: GenerativeModel
    ): ChatService {
        return ChatServiceImpl(generativeModel)
    }
}
