package com.kaaneneskpc.f1setupinstructor.domain.repository

import com.kaaneneskpc.f1setupinstructor.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getUserProfile(): UserProfile
    suspend fun updateProfile(profile: UserProfile)
    val notificationsEnabled: Flow<Boolean>
    val darkThemeEnabled: Flow<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun setDarkThemeEnabled(enabled: Boolean)
}

