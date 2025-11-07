package com.kaaneneskpc.f1setupinstructor.domain.model

data class Setup(
    val gameVersion: String,
    val patch: String?,
    val circuit: String,
    val weatherQuali: String,
    val weatherRace: String,
    val style: SetupStyle,
    val source: SourceMeta,
    val aero: Aero,
    val transmission: Transmission,
    val suspensionGeometry: SuspensionGeometry,
    val suspension: Suspension,
    val brakes: Brakes,
    val tyres: Tyres,
    val notes: String?,
    val score: Double
)
