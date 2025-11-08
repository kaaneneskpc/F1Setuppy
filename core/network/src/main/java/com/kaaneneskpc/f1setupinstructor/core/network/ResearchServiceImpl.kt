package com.kaaneneskpc.f1setupinstructor.core.network

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.kaaneneskpc.f1setupinstructor.domain.model.SetupData
import com.squareup.moshi.Moshi
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

/**
 * Real implementation of ResearchService using Google Gemini AI
 * Searches the internet for optimal F1 game setups and returns structured data
 */
class ResearchServiceImpl @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val moshi: Moshi
) : ResearchService {

    companion object {
        private const val TAG = "ResearchServiceImpl"
        private const val TIMEOUT_MILLIS = 60_000L // 60 seconds timeout
    }

    override suspend fun getSetupFromAi(
        track: String,
        qualyWeather: String,
        raceWeather: String
    ): Result<SetupData> {
        return try {
            Log.d(TAG, "Requesting AI setup for: Track=$track, Quali=$qualyWeather, Race=$raceWeather")
            
            // Create the prompt using the existing function
            val prompt = createPrompt(track, qualyWeather, raceWeather)
            
            // Call Gemini AI with timeout
            val response = withTimeout(TIMEOUT_MILLIS) {
                generativeModel.generateContent(prompt)
            }
            
            val responseText = response.text ?: throw Exception("AI returned empty response")
            Log.d(TAG, "AI Response received (full): $responseText")
            
            // Extract JSON from response (AI might include markdown formatting)
            val jsonText = extractJsonFromResponse(responseText)
            Log.d(TAG, "Extracted JSON: $jsonText")
            
            // Parse the JSON response with lenient parsing
            val adapter = moshi.adapter(SetupData::class.java).lenient()
            val setupData = adapter.fromJson(jsonText)
            
            if (setupData != null) {
                Log.d(TAG, "Successfully parsed setup data for ${setupData.trackName}")
                Result.success(setupData)
            } else {
                Log.e(TAG, "Failed to parse setup data from JSON")
                Result.failure(Exception("Failed to parse AI response into SetupData"))
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error getting setup from AI", e)
            Result.failure(e)
        }
    }

    /**
     * Creates a detailed prompt for the AI to search for optimal F1 setup
     * The AI will search the internet and return structured JSON data
     */
    private fun createPrompt(track: String, qualyWeather: String, raceWeather: String): String {
        return """
        You are an expert F1 25 gaming setup researcher. Your task is to provide the BEST race setup for F1 game (F1 24 or F1 25) for the specified track and weather conditions.
        
        CRITICAL: You MUST respond ONLY with valid JSON. NO explanations, NO markdown, NO extra text.
        
        SEARCH the internet for:
        - Official setup guides from simracers
        - Popular setups from F1Laps.com
        - Setups from SimRacingSetups.com
        - YouTube tutorials and pro player setups
        - Community-recommended setups from Reddit
        
        Track: $track
        Qualifying Weather: $qualyWeather
        Race Weather: $raceWeather
        
        IMPORTANT INSTRUCTIONS:
        1. Find the MOST OPTIMAL setup based on real internet sources
        2. Prioritize setups with high community ratings or from professional sim racers
        3. For the track details, use actual F1 circuit data for $track
        4. Provide realistic setup values that are actually used by players
        5. Include helpful tyre strategy based on the weather conditions
        6. Add key pointers specific to $track's characteristics
        7. Return ONLY valid JSON, no markdown, no explanations, no extra text
        
        Return EXACTLY this JSON structure with realistic setup values for $track:
        {
            "trackName": "$track",
            "carModel": "Ferrari SF-24",
            "gameVersion": "F1 24",
            "weatherCondition": "$qualyWeather / $raceWeather",
            "setupType": "RACE",
            "imageUrl": "",
            "isFavorite": false,
            "frontWingAero": 0,
            "rearWingAero": 0,
            "onThrottle": 0,
            "offThrottle": 0,
            "engineBraking": 0,
            "frontCamber": 0.0,
            "rearCamber": 0.0,
            "frontToe": 0.0,
            "rearToe": 0.0,
            "frontSuspension": 0,
            "rearSuspension": 0,
            "frontAntiRollBar": 0,
            "rearAntiRollBar": 0,
            "frontRideHeight": 0,
            "rearRideHeight": 0,
            "brakePressure": 0,
            "frontBrakeBias": 0,
            "frontLeftTyrePsi": 0.0,
            "frontRightTyrePsi": 0.0,
            "rearLeftTyrePsi": 0.0,
            "rearRightTyrePsi": 0.0,
            "tyreStrategy": "Describe optimal tyre strategy for this track and weather",
            "keyPointers": "Provide 3-4 key driving tips specific to $track",
            "creatorNotes": "Add setup notes and warnings from the source",
            "trackLength": "Actual track length in km",
            "trackCorners": "Number of corners",
            "trackDrsZones": "Number of DRS zones",
            "trackIdealLaps": "Typical race distance in laps"
        }
        
        CRITICAL RULES:
        1. Return ONLY raw JSON (no ```json markdown)
        2. Use realistic F1 game values (aero: 1-50, camber: -3.5 to -1.0, etc.)
        3. Ensure all fields are present
        4. No trailing commas
        5. Double-check JSON is valid
        6. Keep text fields CONCISE (max 2-3 sentences each)
        
        Start your response with { and end with }
        
        Example for text fields:
        "tyreStrategy": "Medium to Hard, 1-stop strategy recommended."
        "keyPointers": "Low downforce setup. Brake early for chicanes."
        "creatorNotes": "Focus on straight-line speed over cornering."
        """.trimIndent()
    }

    /**
     * Extracts JSON content from AI response
     * Handles cases where AI wraps JSON in markdown code blocks or adds extra text
     */
    private fun extractJsonFromResponse(response: String): String {
        var cleaned = response.trim()
        
        // Remove markdown code blocks if present
        val jsonBlockRegex = "```json\\s*([\\s\\S]*?)```".toRegex()
        val jsonMatch = jsonBlockRegex.find(cleaned)
        if (jsonMatch != null) {
            cleaned = jsonMatch.groupValues[1].trim()
        } else if (cleaned.startsWith("```")) {
            // Remove any code block markers
            cleaned = cleaned.removePrefix("```").removeSuffix("```").trim()
            // Remove language identifier if present
            if (cleaned.lines().firstOrNull()?.matches(Regex("\\w+")) == true) {
                cleaned = cleaned.lines().drop(1).joinToString("\n").trim()
            }
        }
        
        // Find JSON object boundaries (handle multiple braces)
        val startIndex = cleaned.indexOf('{')
        var endIndex = -1
        
        if (startIndex != -1) {
            var braceCount = 0
            for (i in startIndex until cleaned.length) {
                when (cleaned[i]) {
                    '{' -> braceCount++
                    '}' -> {
                        braceCount--
                        if (braceCount == 0) {
                            endIndex = i
                            break
                        }
                    }
                }
            }
        }
        
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            cleaned = cleaned.substring(startIndex, endIndex + 1)
        }
        
        // Remove any trailing commas before closing braces (common AI mistake)
        cleaned = cleaned.replace(", }", "}")
        cleaned = cleaned.replace(",}", "}")
        cleaned = cleaned.replace(", ]", "]")
        cleaned = cleaned.replace(",]", "]")
        
        return cleaned
    }
}
