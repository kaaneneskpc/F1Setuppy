package com.kaaneneskpc.f1setupinstructor.feature.results.setupdetails

import androidx.compose.runtime.Stable
import com.kaaneneskpc.f1setupinstructor.domain.model.Setup

@Stable
data class SetupDetailsUiState(
    val imageUrl: String = "",
    val badge: String = "QUALIFYING SETUP",
    val title: String = "Monza Circuit",
    val subtitle: String = "F1 23 - Dry Weather",
    val isFavorite: Boolean = false,
    val tabs: List<String> = listOf("Aerodynamics", "Transmission", "Suspension", "Brakes", "Tyres"),
    val selectedTabIndex: Int = 0,
    
    // Setup data
    val aerodynamics: AeroData = AeroData(),
    val transmission: TransmissionData = TransmissionData(),
    val suspensionGeometry: SuspensionGeometryData = SuspensionGeometryData(),
    val suspension: SuspensionData = SuspensionData(),
    val brakes: BrakesData = BrakesData(),
    val tyres: TyresData = TyresData(),
    
    // Track and notes
    val trackDetails: TrackDetails = TrackDetails(),
    val tyreStrategy: String = "Soft -> Medium (1-stop) is the most common. Watch for high tyre degradation on the softs, especially at the rear.",
    val keyPointers: String = "Monza is the Temple of Speed. Low downforce is critical for the long straights. A good exit from Parabolica is key for a fast lap.",
    val creatorNotes: String = "This setup is optimized for single-lap pace. Focus on hitting the apexes at Ascari and Parabolica. You might need to short-shift out of the chicanes to manage wheelspin. Good luck!",
    
    val isLoading: Boolean = false,
    val error: String? = null
)

// Data classes for each tab
data class AeroData(
    val frontWingAero: Int = 28,
    val rearWingAero: Int = 25
)

data class TransmissionData(
    val onThrottle: Int = 50,
    val offThrottle: Int = 50,
    val engineBraking: Int = 50
)

data class SuspensionGeometryData(
    val frontCamber: Double = -2.50,
    val rearCamber: Double = -1.00,
    val frontToe: Double = 0.05,
    val rearToe: Double = 0.20
)

data class SuspensionData(
    val frontSusp: Int = 1,
    val rearSusp: Int = 1,
    val frontARB: Int = 1,
    val rearARB: Int = 1,
    val frontRideHeight: Int = 1,
    val rearRideHeight: Int = 1
)

data class BrakesData(
    val pressure: Int = 100,
    val bias: Int = 50
)

data class TyresData(
    val frontLeftPsi: Double = 23.5,
    val frontRightPsi: Double = 23.5,
    val rearLeftPsi: Double = 21.0,
    val rearRightPsi: Double = 21.0
)

data class TrackDetails(
    val length: String = "5.793 km",
    val corners: String = "11",
    val drsZones: String = "2",
    val idealLaps: String = "53"
)

// Extension to create UI state from domain Setup
fun Setup.toUiState(): SetupDetailsUiState {
    return SetupDetailsUiState(
        imageUrl = "", // Will be set from circuit image mapping
        badge = when (style) {
            com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle.LOW_DF -> "LOW DOWNFORCE SETUP"
            com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle.BALANCED -> "BALANCED SETUP"
            com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle.TYRE_SAVE -> "TYRE SAVING SETUP"
        },
        title = "$circuit - $gameVersion",
        subtitle = "$gameVersion - Q: $weatherQuali / R: $weatherRace",
        aerodynamics = AeroData(
            frontWingAero = aero.front,
            rearWingAero = aero.rear
        ),
        transmission = TransmissionData(
            onThrottle = transmission.onThrottle,
            offThrottle = transmission.offThrottle,
            engineBraking = transmission.engineBraking
        ),
        suspensionGeometry = SuspensionGeometryData(
            frontCamber = suspensionGeometry.frontCamber,
            rearCamber = suspensionGeometry.rearCamber,
            frontToe = suspensionGeometry.frontToe,
            rearToe = suspensionGeometry.rearToe
        ),
        suspension = SuspensionData(
            frontSusp = suspension.frontSusp,
            rearSusp = suspension.rearSusp,
            frontARB = suspension.frontARB,
            rearARB = suspension.rearARB,
            frontRideHeight = suspension.frontRideHeight,
            rearRideHeight = suspension.rearRideHeight
        ),
        brakes = BrakesData(
            pressure = brakes.pressure,
            bias = brakes.bias
        ),
        tyres = TyresData(
            frontLeftPsi = tyres.frontPsi,
            frontRightPsi = tyres.frontPsi,
            rearLeftPsi = tyres.rearPsi,
            rearRightPsi = tyres.rearPsi
        ),
        creatorNotes = notes ?: ""
    )
}
