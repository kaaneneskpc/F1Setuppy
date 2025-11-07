package com.kaaneneskpc.f1setupinstructor.core.network

import com.kaaneneskpc.f1setupinstructor.domain.model.Aero
import com.kaaneneskpc.f1setupinstructor.domain.model.Brakes
import com.kaaneneskpc.f1setupinstructor.domain.model.Setup
import com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle
import com.kaaneneskpc.f1setupinstructor.domain.model.SourceMeta
import com.kaaneneskpc.f1setupinstructor.domain.model.Suspension
import com.kaaneneskpc.f1setupinstructor.domain.model.SuspensionGeometry
import com.kaaneneskpc.f1setupinstructor.domain.model.Transmission
import com.kaaneneskpc.f1setupinstructor.domain.model.Tyres
import java.time.Instant
import javax.inject.Inject

class FakeResearchService @Inject constructor() : ResearchService {

    private val fakeSetups = listOf(
        Setup(
            gameVersion = "F1 24",
            patch = "1.13",
            circuit = "Monza",
            weatherQuali = "Dry",
            weatherRace = "Dry",
            style = SetupStyle.LOW_DF,
            source = SourceMeta(
                name = "F1Laps",
                url = "https://www.f1laps.com/setups/monza-dry",
                publishedAt = Instant.now(),
                communityRating = 4.5
            ),
            aero = Aero(front = 15, rear = 10),
            transmission = Transmission(onThrottle = 45, offThrottle = 45, engineBraking = 50),
            suspensionGeometry = SuspensionGeometry(frontCamber = -3.50, rearCamber = -2.00, frontToe = 0.00, rearToe = 0.10),
            suspension = Suspension(frontSusp = 41, rearSusp = 11, frontARB = 11, rearARB = 10, frontRideHeight = 20, rearRideHeight = 47),
            brakes = Brakes(pressure = 100, bias = 54),
            tyres = Tyres(frontPsi = 29.5, rearPsi = 21.5),
            notes = "A good setup for Monza, focusing on low drag and high top speed.",
            score = 0.9
        ),
        Setup(
            gameVersion = "F1 24",
            patch = "1.13",
            circuit = "Silverstone",
            weatherQuali = "Dry",
            weatherRace = "Dry",
            style = SetupStyle.BALANCED,
            source = SourceMeta(
                name = "SimRacingSetups",
                url = "https://simracingsetups.com/setups/f1-24/silverstone-dry-balanced",
                publishedAt = Instant.now(),
                communityRating = 4.2
            ),
            aero = Aero(front = 25, rear = 20),
            transmission = Transmission(onThrottle = 50, offThrottle = 50, engineBraking = 55),
            suspensionGeometry = SuspensionGeometry(frontCamber = -3.20, rearCamber = -1.80, frontToe = 0.05, rearToe = 0.15),
            suspension = Suspension(frontSusp = 35, rearSusp = 15, frontARB = 9, rearARB = 8, frontRideHeight = 25, rearRideHeight = 50),
            brakes = Brakes(pressure = 100, bias = 55),
            tyres = Tyres(frontPsi = 30.0, rearPsi = 22.0),
            notes = "A balanced setup for Silverstone, providing good downforce for the high-speed corners.",
            score = 0.85
        )
    )

    fun getSetups(circuit: String, qualiWeather: String, raceWeather: String, style: SetupStyle?): List<Setup> {
        return fakeSetups.filter { it.circuit == circuit && it.weatherQuali == qualiWeather && it.weatherRace == raceWeather && (style == null || it.style == style) }
    }
}
