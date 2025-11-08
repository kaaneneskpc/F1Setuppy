package com.kaaneneskpc.f1setupinstructor.domain.repository

import com.kaaneneskpc.f1setupinstructor.domain.model.SetupData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Simple in-memory cache for the latest setup data
 * Used to pass data between HomeScreen and SetupDetailsScreen
 */
@Singleton
class CachedSetupManager @Inject constructor() {
    
    private var latestSetup: SetupData? = null
    
    fun saveLatestSetup(setupData: SetupData) {
        latestSetup = setupData
    }
    
    fun getLatestSetup(): SetupData? {
        return latestSetup
    }
    
    fun clearLatestSetup() {
        latestSetup = null
    }
}

