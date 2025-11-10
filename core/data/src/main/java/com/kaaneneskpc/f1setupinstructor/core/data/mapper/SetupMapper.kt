package com.kaaneneskpc.f1setupinstructor.core.data.mapper

import com.kaaneneskpc.f1setupinstructor.core.database.entity.AeroEntity
import com.kaaneneskpc.f1setupinstructor.core.database.entity.BrakesEntity
import com.kaaneneskpc.f1setupinstructor.core.database.entity.SetupEntity
import com.kaaneneskpc.f1setupinstructor.core.database.entity.SuspensionEntity
import com.kaaneneskpc.f1setupinstructor.core.database.entity.SuspensionGeometryEntity
import com.kaaneneskpc.f1setupinstructor.core.database.entity.TransmissionEntity
import com.kaaneneskpc.f1setupinstructor.core.database.entity.TyresEntity
import com.kaaneneskpc.f1setupinstructor.domain.model.Aero
import com.kaaneneskpc.f1setupinstructor.domain.model.Brakes
import com.kaaneneskpc.f1setupinstructor.domain.model.Setup
import com.kaaneneskpc.f1setupinstructor.domain.model.SetupData
import com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle
import com.kaaneneskpc.f1setupinstructor.domain.model.SourceMeta
import com.kaaneneskpc.f1setupinstructor.domain.model.Suspension
import com.kaaneneskpc.f1setupinstructor.domain.model.SuspensionGeometry
import com.kaaneneskpc.f1setupinstructor.domain.model.Transmission
import com.kaaneneskpc.f1setupinstructor.domain.model.Tyres
import java.time.Instant

fun SetupEntity.toDomainModel(): Setup = Setup(
    gameVersion = gameVersion,
    patch = patch,
    circuit = circuit,
    weatherQuali = weatherQuali,
    weatherRace = weatherRace,
    style = style,
    source = SourceMeta(
        name = sourceName,
        url = sourceUrl,
        publishedAt = sourcePublishedAt,
        communityRating = sourceCommunityRating
    ),
    aero = aero.toDomainModel(),
    transmission = transmission.toDomainModel(),
    suspensionGeometry = suspensionGeometry.toDomainModel(),
    suspension = suspension.toDomainModel(),
    brakes = brakes.toDomainModel(),
    tyres = tyres.toDomainModel(),
    notes = notes,
    score = score
)

fun AeroEntity.toDomainModel(): Aero = Aero(front = front, rear = rear)
fun TransmissionEntity.toDomainModel(): Transmission = Transmission(onThrottle = onThrottle, offThrottle = offThrottle, engineBraking = engineBraking)
fun SuspensionGeometryEntity.toDomainModel(): SuspensionGeometry = SuspensionGeometry(frontCamber = frontCamber, rearCamber = rearCamber, frontToe = frontToe, rearToe = rearToe)
fun SuspensionEntity.toDomainModel(): Suspension = Suspension(frontSusp = frontSusp, rearSusp = rearSusp, frontARB = frontARB, rearARB = rearARB, frontRideHeight = frontRideHeight, rearRideHeight = rearRideHeight)
fun BrakesEntity.toDomainModel(): Brakes = Brakes(pressure = pressure, bias = bias)
fun TyresEntity.toDomainModel(): Tyres = Tyres(frontPsi = frontPsi, rearPsi = rearPsi)

fun Setup.toEntity(): SetupEntity = SetupEntity(
    sourceUrl = source.url,
    sourceName = source.name,
    sourcePublishedAt = source.publishedAt,
    sourceCommunityRating = source.communityRating,
    gameVersion = gameVersion,
    patch = patch,
    circuit = circuit,
    weatherQuali = weatherQuali,
    weatherRace = weatherRace,
    style = style,
    aero = aero.toEntity(),
    transmission = transmission.toEntity(),
    suspensionGeometry = suspensionGeometry.toEntity(),
    suspension = suspension.toEntity(),
    brakes = brakes.toEntity(),
    tyres = tyres.toEntity(),
    notes = notes,
    score = score
)

fun Aero.toEntity(): AeroEntity = AeroEntity(front = front, rear = rear)
fun Transmission.toEntity(): TransmissionEntity = TransmissionEntity(onThrottle = onThrottle, offThrottle = offThrottle, engineBraking = engineBraking)
fun SuspensionGeometry.toEntity(): SuspensionGeometryEntity = SuspensionGeometryEntity(frontCamber = frontCamber, rearCamber = rearCamber, frontToe = frontToe, rearToe = rearToe)
fun Suspension.toEntity(): SuspensionEntity = SuspensionEntity(frontSusp = frontSusp, rearSusp = rearSusp, frontARB = frontARB, rearARB = rearARB, frontRideHeight = frontRideHeight, rearRideHeight = rearRideHeight)
fun Brakes.toEntity(): BrakesEntity = BrakesEntity(pressure = pressure, bias = bias)
fun Tyres.toEntity(): TyresEntity = TyresEntity(frontPsi = frontPsi, rearPsi = rearPsi)

/**
 * Converts AI-generated SetupData to domain Setup model
 * Used for caching AI responses to database
 */
fun SetupData.toDomainSetup(): Setup {
    // Parse weather conditions
    val weatherParts = weatherCondition.split("/").map { it.trim() }
    val qualyWeather = weatherParts.getOrElse(0) { "Dry" }
    val raceWeather = weatherParts.getOrElse(1) { qualyWeather }
    
    // Determine setup style based on aero values
    val style = when {
        frontWingAero < 20 && rearWingAero < 20 -> SetupStyle.LOW_DF
        frontWingAero > 35 || rearWingAero > 35 -> SetupStyle.BALANCED
        else -> SetupStyle.TYRE_SAVE
    }
    
    // Calculate average tyre pressure
    val avgFrontPsi = (frontLeftTyrePsi + frontRightTyrePsi) / 2.0
    val avgRearPsi = (rearLeftTyrePsi + rearRightTyrePsi) / 2.0
    
    return Setup(
        gameVersion = gameVersion,
        patch = null, // AI doesn't provide patch info
        circuit = trackName,
        weatherQuali = qualyWeather,
        weatherRace = raceWeather,
        style = style,
        source = SourceMeta(
            name = "AI Generated - $carModel",
            url = "ai://gemini/$trackName/${System.currentTimeMillis()}",
            publishedAt = Instant.now(),
            communityRating = null
        ),
        aero = Aero(
            front = frontWingAero,
            rear = rearWingAero
        ),
        transmission = Transmission(
            onThrottle = onThrottle,
            offThrottle = offThrottle,
            engineBraking = engineBraking
        ),
        suspensionGeometry = SuspensionGeometry(
            frontCamber = frontCamber.toDouble(),
            rearCamber = rearCamber.toDouble(),
            frontToe = frontToe.toDouble(),
            rearToe = rearToe.toDouble()
        ),
        suspension = Suspension(
            frontSusp = frontSuspension,
            rearSusp = rearSuspension,
            frontARB = frontAntiRollBar,
            rearARB = rearAntiRollBar,
            frontRideHeight = frontRideHeight,
            rearRideHeight = rearRideHeight
        ),
        brakes = Brakes(
            pressure = brakePressure,
            bias = frontBrakeBias
        ),
        tyres = Tyres(
            frontPsi = avgFrontPsi,
            rearPsi = avgRearPsi
        ),
        notes = "$keyPointers\n\nTyre Strategy: $tyreStrategy\n\nCreator Notes: $creatorNotes",
        score = 4.5 // Default score for AI-generated setups
    )
}
