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
        sessionType: String,
        qualyWeather: String,
        raceWeather: String
    ): Result<SetupData> {
        return try {
            Log.d(TAG, "Requesting AI setup for: Track=$track, SessionType=$sessionType, Quali=$qualyWeather, Race=$raceWeather")
            
            // Create the prompt using the existing function
            val prompt = createPrompt(track, sessionType, qualyWeather, raceWeather)
            
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
    private fun createPrompt(track: String, sessionType: String, qualyWeather: String, raceWeather: String): String {
        val setupTypeDescription = when (sessionType) {
            "Qualifying" -> "QUALIFYING setup (1-lap pace, maximum performance, aggressive setup)"
            "Race" -> "RACE setup (consistency, tyre management, long-run pace)"
            else -> "BALANCED setup"
        }
        
        return """
        You are an expert EA SPORTS F1 25 gaming setup researcher. Your task is to search the internet and provide the BEST and most POPULAR $setupTypeDescription specifically for F1 25 game.
        
        CRITICAL REQUIREMENTS:
        1. Search ONLY for EA SPORTS F1 25 setups (NOT F1 24, F1 23, or other versions)
        2. Find the most recent and popular setups from the community
        3. This setup is specifically for: $sessionType
        4. Respond ONLY with valid JSON - NO explanations, NO markdown, NO extra text
        
        SEARCH these sources for F1 25 setups:
        - F1Laps.com (F1 25 section)
        - SimRacingSetups.com (F1 25 setups)
        - YouTube (F1 25 setup guides for $track)
        - Reddit r/F1Game (F1 25 discussions)
        - TrueRacing.gg (F1 25 setups)
        - Overtake.gg (F1 25 community setups)
        
        GAME: EA SPORTS F1 25 (2024/2025 season)
        Track: $track
        Session Type: $sessionType
        ${if (sessionType == "Qualifying") "Weather: $qualyWeather" else "Qualifying Weather: $qualyWeather\n        Race Weather: $raceWeather"}
        
        SEARCH STRATEGY:
        1. Look for "F1 25 $track $sessionType setup" on the internet
        2. Find setups from professional sim racers or high-rated community members
        3. Prioritize recent setups (2024-2025 season)
        4. Check for the latest game patch/update compatibility
        5. Verify setup values are realistic for F1 25 game mechanics
        6. ${if (sessionType == "Qualifying") "Focus on MAXIMUM DOWNFORCE and AGGRESSIVE setup for 1-lap pace" else "Focus on BALANCE, TYRE WEAR and CONSISTENCY for race distance"}
        
        IMPORTANT INSTRUCTIONS:
        1. Use ONLY F1 25 specific setup values (NOT F1 24 or older)
        2. For the track details, use actual F1 circuit data for $track
        3. Include weather-specific tyre strategy ($qualyWeather qualifying, $raceWeather race)
        4. Add key driving tips specific to $track's layout and characteristics
        5. Provide realistic setup values used by top F1 25 players
        6. Return ONLY valid JSON - no markdown, no explanations, no extra text
        
        Return EXACTLY this JSON structure with realistic F1 25 setup values for $track:
        {
            "trackName": "$track",
            "carModel": "Ferrari SF-24",
            "gameVersion": "F1 25",
            "weatherCondition": "${if (sessionType == "Qualifying") qualyWeather else "$qualyWeather / $raceWeather"}",
            "setupType": "$sessionType",
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
        
        CRITICAL RULES FOR F1 25:
        1. Return ONLY raw JSON (no ```json markdown, no code blocks)
        2. Use F1 25 specific setup ranges:
           - Aero: Front 0-50, Rear 0-50 (lower = less downforce)
           - Camber: -3.5° to -1.0° (negative values)
           - Toe: -0.50° to 0.50°
           - Suspension: 1-11 (softer to stiffer)
           - Anti-Roll Bar: 1-11 (softer to stiffer)
           - Ride Height: 0-100mm
           - Brake Pressure: 80-100%
           - Brake Bias: 50-70% (front bias)
           - Tyre Pressure: 19.0-25.0 PSI
        3. Ensure ALL fields are present with realistic F1 25 values
        4. No trailing commas
        5. Double-check JSON is valid
        6. Keep text fields CONCISE (max 2-3 sentences each)
        7. Make setup values specific to $track characteristics in F1 25
        
        Start your response with { and end with }
        
        TRACK-SPECIFIC SETUP GUIDELINES FOR F1 25:
        
        ${if (sessionType == "Qualifying") {
            """
            QUALIFYING SETUP CHARACTERISTICS:
            - More aggressive aero (higher downforce for better lap time)
            - Softer suspension for maximum mechanical grip
            - Higher brake pressure for confident late braking
            - Lower ride height for better aero efficiency
            - Focus on single lap performance over tyre wear
            """.trimIndent()
        } else {
            """
            RACE SETUP CHARACTERISTICS:
            - Balanced aero (consider tyre wear and fuel load)
            - Slightly stiffer suspension for long-run stability
            - Moderate brake pressure to preserve brakes
            - Slightly higher ride height for fuel load variation
            - Focus on consistency and tyre management
            """.trimIndent()
        }}
        
        For LOW DOWNFORCE tracks (Monza, Spa, Jeddah, Baku):
        - Front Wing: ${if (sessionType == "Qualifying") "15-30" else "10-25"}, Rear Wing: ${if (sessionType == "Qualifying") "12-25" else "8-20"}
        - Strategy: "Low drag setup for top speed on straights"
        - Pointers: Focus on straight-line speed, minimal wing angles
        
        For HIGH DOWNFORCE tracks (Monaco, Singapore, Hungary):
        - Front Wing: ${if (sessionType == "Qualifying") "40-50" else "35-45"}, Rear Wing: ${if (sessionType == "Qualifying") "40-50" else "35-45"}
        - Strategy: "Maximum grip setup for tight corners"
        - Pointers: Prioritize cornering grip over top speed
        
        For BALANCED tracks (Silverstone, Suzuka, Barcelona):
        - Front Wing: ${if (sessionType == "Qualifying") "30-40" else "25-35"}, Rear Wing: ${if (sessionType == "Qualifying") "30-40" else "25-35"}
        - Strategy: "Balanced setup for mixed corners"
        - Pointers: Compromise between speed and downforce
        
        Example JSON for $track:
        "tyreStrategy": "Based on $track length and $raceWeather weather, recommend optimal compound sequence"
        "keyPointers": "Based on $track's key corners (e.g., Parabolica for Monza, Eau Rouge for Spa)"
        "creatorNotes": "Specific to F1 25 handling model and $track layout"
        
        REMEMBER: This is for EA SPORTS F1 25 ONLY. Do NOT use F1 24 or F1 23 setups!
        
        Start your response with { and end with }
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
