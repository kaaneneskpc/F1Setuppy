package com.kaaneneskpc.f1setupinstructor.core.database.entity

data class SuspensionEntity(
    val frontSusp: Int,
    val rearSusp: Int,
    val frontARB: Int,
    val rearARB: Int,
    val frontRideHeight: Int,
    val rearRideHeight: Int
)
