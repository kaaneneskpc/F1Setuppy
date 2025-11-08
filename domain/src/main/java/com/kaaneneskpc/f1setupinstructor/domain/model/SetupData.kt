package com.kaaneneskpc.f1setupinstructor.domain.model

/**
 * Data Transfer Object for Setup information
 * Used for communication between network layer and data layer
 * Can be mapped to domain Setup model
 */
data class SetupData(
    val trackName: String,
    val carModel: String,
    val gameVersion: String,
    val weatherCondition: String,
    val setupType: String, // "QUALIFYING" or "RACE"
    val imageUrl: String,
    val isFavorite: Boolean = false,
    
    // Aerodynamics
    val frontWingAero: Int,
    val rearWingAero: Int,
    
    // Transmission
    val onThrottle: Int,
    val offThrottle: Int,
    val engineBraking: Int,

    // Suspension Geometry
    val frontCamber: Float,
    val rearCamber: Float,
    val frontToe: Float,
    val rearToe: Float,

    // Suspension
    val frontSuspension: Int,
    val rearSuspension: Int,
    val frontAntiRollBar: Int,
    val rearAntiRollBar: Int,
    val frontRideHeight: Int,
    val rearRideHeight: Int,

    // Brakes
    val brakePressure: Int,
    val frontBrakeBias: Int,

    // Tyres
    val frontLeftTyrePsi: Float,
    val frontRightTyrePsi: Float,
    val rearLeftTyrePsi: Float,
    val rearRightTyrePsi: Float,

    // Textual info
    val tyreStrategy: String,
    val keyPointers: String,
    val creatorNotes: String,

    // Track Details
    val trackLength: String,
    val trackCorners: String,
    val trackDrsZones: String,
    val trackIdealLaps: String
)

