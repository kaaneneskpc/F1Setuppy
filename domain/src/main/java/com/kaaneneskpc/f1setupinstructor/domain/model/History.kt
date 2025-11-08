package com.kaaneneskpc.f1setupinstructor.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class History(
    val id: Long = 0,
    val track: String,
    val car: String,
    val weatherCondition: String,
    val createdAt: Long = System.currentTimeMillis(),
    // TODO: Add other relevant setup details from the details screen
    //  e.g., val tyrePressure: String, val suspensionGeometry: String etc.
) : Parcelable
