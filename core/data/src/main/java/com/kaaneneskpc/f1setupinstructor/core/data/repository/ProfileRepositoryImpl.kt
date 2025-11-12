package com.kaaneneskpc.f1setupinstructor.core.data.repository

import com.kaaneneskpc.f1setupinstructor.core.data.preferences.UserPreferencesDataStore
import com.kaaneneskpc.f1setupinstructor.domain.model.UserProfile
import com.kaaneneskpc.f1setupinstructor.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore
) : ProfileRepository {
    private var currentProfile = UserProfile(
        name = "Kaan Enes KAPICI",
        handle = "@kaaneneskpc",
        email = "kaaneneskpc1@gmail.com",
        avatarUrl = null,
        favoriteTracks = listOf("Silverstone", "Monza")
    )
    
    override suspend fun getUserProfile(): UserProfile {
        return currentProfile
    }
    
    override suspend fun updateProfile(profile: UserProfile) {
        currentProfile = profile
    }
    
    override val notificationsEnabled: Flow<Boolean>
        get() = userPreferencesDataStore.notificationsEnabled
    
    override val darkThemeEnabled: Flow<Boolean>
        get() = userPreferencesDataStore.darkThemeEnabled
    
    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        userPreferencesDataStore.setNotificationsEnabled(enabled)
    }
    
    override suspend fun setDarkThemeEnabled(enabled: Boolean) {
        userPreferencesDataStore.setDarkThemeEnabled(enabled)
    }
}

