package com.kaaneneskpc.f1setupinstructor.core.network.config

import com.kaaneneskpc.f1setupinstructor.core.network.BuildConfig

/**
 * API Configuration for external services
 * 
 * IMPORTANT: Never commit actual API keys to version control!
 * 
 * To use:
 * 1. Add your API key to local.properties:
 *    GEMINI_API_KEY=your_actual_api_key_here
 * 
 * 2. Update build.gradle.kts to read from local.properties:
 *    android {
 *        defaultConfig {
 *            buildConfigField("String", "GEMINI_API_KEY", "\"${project.findProperty("GEMINI_API_KEY") ?: ""}\"")
 *        }
 *    }
 * 
 * 3. Enable BuildConfig:
 *    android {
 *        buildFeatures {
 *            buildConfig = true
 *        }
 *    }
 */
object ApiConfig {
    
    /**
     * Gets Gemini API key from BuildConfig
     * Falls back to empty string if not configured
     */
    fun getGeminiApiKey(): String {
        // This will be replaced by BuildConfig after setup
        return try {
            BuildConfig.GEMINI_API_KEY
            ""
        } catch (e: Exception) {
            ""
        }
    }
    
    /**
     * Validates if API key is configured
     */
    fun isApiKeyConfigured(): Boolean {
        val key = getGeminiApiKey()
        return key.isNotEmpty() && key != "YOUR_GEMINI_API_KEY"
    }
}

