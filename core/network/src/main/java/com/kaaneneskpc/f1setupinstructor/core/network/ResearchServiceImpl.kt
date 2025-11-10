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
        setupType: String,
        qualyWeather: String,
        raceWeather: String
    ): Result<SetupData> {
        return try {
            Log.d(TAG, "=== AI Setup Request Started ===")
            Log.d(TAG, "Track: $track | Type: $setupType | Quali: $qualyWeather | Race: $raceWeather")
            
            // Create the prompt using the existing function
            val prompt = createPrompt(track, setupType, qualyWeather, raceWeather)
            
            // Call Gemini AI with timeout
            val response = withTimeout(TIMEOUT_MILLIS) {
                generativeModel.generateContent(prompt)
            }
            
            val responseText = response.text ?: throw Exception("AI returned empty response")
            Log.d(TAG, "AI Response Length: ${responseText.length} chars")
            Log.d(TAG, "AI Response Preview (first 300 chars): ${responseText.take(300)}")
            
            // Extract JSON from response (AI might include markdown formatting)
            val jsonText = try {
                extractJsonFromResponse(responseText)
            } catch (e: Exception) {
                Log.e(TAG, "Error extracting JSON: ${e.message}", e)
                throw Exception("JSON extraction failed: ${e.message}", e)
            }
            
            Log.d(TAG, "Extracted JSON Length: ${jsonText.length} chars")
            Log.d(TAG, "Extracted JSON Preview (first 500 chars): ${jsonText.take(500)}")
            
            // Parse the JSON response with lenient parsing
            val setupData = try {
                val adapter = moshi.adapter(SetupData::class.java).lenient()
                adapter.fromJson(jsonText)
            } catch (e: Exception) {
                Log.e(TAG, "JSON Parsing Error: ${e.message}", e)
                Log.e(TAG, "Failed JSON Content: $jsonText")
                throw Exception("JSON parsing failed: ${e.message}", e)
            }
            
            if (setupData != null) {
                Log.d(TAG, "✅ Successfully parsed setup: ${setupData.trackName} - ${setupData.setupType}")
                Result.success(setupData)
            } else {
                Log.e(TAG, "❌ Parsed setup is null")
                Result.failure(Exception("Failed to parse AI response into SetupData - result is null"))
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error getting setup from AI: ${e.javaClass.simpleName}", e)
            Log.e(TAG, "Error message: ${e.message}")
            Log.e(TAG, "Stack trace: ${e.stackTraceToString()}")
            Result.failure(e)
        }
    }

    /**
     * Creates a detailed prompt for the AI to search for optimal F1 setup
     * The AI will search the internet and return structured JSON data
     */
    private fun createPrompt(track: String, setupType: String, qualyWeather: String, raceWeather: String): String {
        val relevantWeather = if (setupType == "QUALIFYING") qualyWeather else raceWeather
        val weatherLabel = if (setupType == "QUALIFYING") "Qualifying" else "Race"
        
        return """
        You are an expert EA SPORTS F1 25 gaming setup researcher. Your task is to search the internet and provide the BEST and most POPULAR $setupType setup specifically for F1 25 game.
        
        CRITICAL REQUIREMENTS:
        1. Search ONLY for EA SPORTS F1 25 setups (NOT F1 24, F1 23, or other versions)
        2. Find the most recent and popular setups from the community
        3. Respond ONLY with valid JSON - NO explanations, NO markdown, NO extra text
        4. IMPORTANT: This is a $setupType setup - optimize accordingly!
        
        SEARCH these sources for F1 25 setups:
        - F1Laps.com (F1 25 section)
        - SimRacingSetups.com (F1 25 setups)
        - YouTube (F1 25 setup guides for $track)
        - Reddit r/F1Game (F1 25 discussions)
        - TrueRacing.gg (F1 25 setups)
        - Overtake.gg (F1 25 community setups)
        
        GAME: EA SPORTS F1 25 (2024/2025 season)
        Track: $track
        Setup Type: $setupType
        Weather Condition: $relevantWeather ($weatherLabel)
        
        SEARCH STRATEGY:
        1. Look for "F1 25 $track $setupType $relevantWeather setup" on the internet
        2. Find setups from professional sim racers or high-rated community members
        3. Prioritize recent setups (2024-2025 season)
        4. Check for the latest game patch/update compatibility
        5. Verify setup values are realistic for F1 25 game mechanics
        6. For QUALIFYING: Focus on one-lap pace, aggressive setup, maximum downforce for cornering
        7. For RACE: Focus on tyre management, fuel load, race pace, balanced setup
        
        IMPORTANT INSTRUCTIONS:
        1. Use ONLY F1 25 specific setup values (NOT F1 24 or older)
        2. For the track details, use actual F1 circuit data for $track
        3. Include weather-specific tyre strategy for $relevantWeather conditions
        4. Add key driving tips specific to $track's layout and $relevantWeather weather
        5. Provide realistic setup values used by top F1 25 players
        6. Return ONLY valid JSON - no markdown, no explanations, no extra text
        7. SETUP TYPE SPECIFIC:
           - If $setupType is QUALIFYING: Prioritize downforce, stability for 1-lap pace, aggressive wing angles, optimize for $relevantWeather
           - If $setupType is RACE: Prioritize tyre wear, balance, consistency over race distance, optimize for $relevantWeather
        
        Return EXACTLY this JSON structure with realistic F1 25 setup values for $track:
        {
            "trackName": "$track",
            "carModel": "Ferrari SF-24",
            "gameVersion": "F1 25",
            "weatherCondition": "$relevantWeather",
            "setupType": "$setupType",
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
        
        For LOW DOWNFORCE tracks (Monza, Spa, Jeddah, Baku):
        - Front Wing: 10-25, Rear Wing: 8-20
        - Strategy: "Low drag setup for top speed on straights"
        - Pointers: Focus on straight-line speed, minimal wing angles
        
        For HIGH DOWNFORCE tracks (Monaco, Singapore, Hungary):
        - Front Wing: 35-45, Rear Wing: 35-45
        - Strategy: "Maximum grip setup for tight corners"
        - Pointers: Prioritize cornering grip over top speed
        
        For BALANCED tracks (Silverstone, Suzuka, Barcelona):
        - Front Wing: 25-35, Rear Wing: 25-35
        - Strategy: "Balanced setup for mixed corners"
        - Pointers: Compromise between speed and downforce
        
        SETUP TYPE ADJUSTMENTS:
        
        If setupType is QUALIFYING:
        - Add +2 to +5 on wing values for more downforce and grip
        - Stiffer suspension for better cornering response
        - Lower ride height for better aero efficiency
        - More aggressive camber angles
        - Focus on single-lap performance
        - tyreStrategy: "Soft compound for maximum grip, push mode"
        - keyPointers: "Focus on qualifying-specific braking points and corner entry"
        - creatorNotes: "This is an aggressive qualifying setup - may wear tyres quickly"
        
        If setupType is RACE:
        - Balanced wing values for tyre management
        - Softer suspension for tyre preservation
        - Slightly higher ride height for fuel load
        - Conservative camber for even tyre wear
        - Focus on race consistency
        - tyreStrategy: "Multi-stint strategy with tyre management focus"
        - keyPointers: "Smooth driving style, conserve tyres in key corners"
        - creatorNotes: "Race setup prioritizes consistency and tyre life"
        
        Example JSON for $track:
        "tyreStrategy": "Based on $track length, $setupType mode, and $relevantWeather weather"
        "keyPointers": "Based on $track's key corners, $setupType requirements, and $relevantWeather conditions"
        "creatorNotes": "Specific to F1 25, $track layout, $setupType mode, and $relevantWeather weather"
        
        REMEMBER: This is for EA SPORTS F1 25 ONLY. Do NOT use F1 24 or F1 23 setups!
        IMPORTANT: Adjust ALL values according to $setupType (QUALIFYING or RACE) requirements!
        
        Start your response with { and end with }
        """.trimIndent()
    }

    /**
     * Extracts JSON content from AI response
     * Handles cases where AI wraps JSON in markdown code blocks or adds extra text
     * Uses safe string operations to avoid regex syntax errors
     */
    private fun extractJsonFromResponse(response: String): String {
        var cleaned = response.trim()
        
        try {
            // Remove markdown code blocks if present using safe regex
            val codeBlockPattern = Regex("```json\\s*([\\s\\S]*?)```")
            val codeBlockMatch = codeBlockPattern.find(cleaned)
            if (codeBlockMatch != null) {
                cleaned = codeBlockMatch.groupValues[1].trim()
            } else if (cleaned.startsWith("```")) {
                // Remove any code block markers without regex
                cleaned = cleaned.removePrefix("```").removeSuffix("```").trim()
                // Remove language identifier if present (first line if it's a single word)
                val lines = cleaned.lines()
                if (lines.isNotEmpty() && lines[0].trim().matches(Regex("\\w+"))) {
                    cleaned = lines.drop(1).joinToString("\n").trim()
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error removing markdown blocks, continuing with original: ${e.message}")
        }
        
        // Find JSON object boundaries (handle nested braces correctly)
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
        
        // Remove trailing commas using safe string replacement (no regex)
        // This handles common AI mistakes like {"key": "value",}
        cleaned = cleaned.replace(",\n}", "}")
        cleaned = cleaned.replace(", }", "}")
        cleaned = cleaned.replace(",}", "}")
        cleaned = cleaned.replace(",\n]", "]")
        cleaned = cleaned.replace(", ]", "]")
        cleaned = cleaned.replace(",]", "]")
        
        // Remove multiple consecutive commas
        while (cleaned.contains(",,")) {
            cleaned = cleaned.replace(",,", ",")
        }
        
        Log.d(TAG, "Cleaned JSON (first 200 chars): ${cleaned.take(200)}")
        
        return cleaned
    }
}
