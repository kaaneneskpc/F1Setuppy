package com.kaaneneskpc.f1setupinstructor.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle
import java.time.Instant

@Entity(
    tableName = "setups",
    indices = [
        androidx.room.Index(value = ["circuit", "weatherQuali", "weatherRace", "patch"])
    ]
)
data class SetupEntity(
    @PrimaryKey
    val sourceUrl: String,
    val sourceName: String,
    val sourcePublishedAt: Instant,
    val sourceCommunityRating: Double?,
    val gameVersion: String,
    val patch: String?,
    val circuit: String,
    val weatherQuali: String,
    val weatherRace: String,
    val style: SetupStyle,
    @Embedded(prefix = "aero_")
    val aero: AeroEntity,
    @Embedded(prefix = "transmission_")
    val transmission: TransmissionEntity,
    @Embedded(prefix = "suspension_geometry_")
    val suspensionGeometry: SuspensionGeometryEntity,
    @Embedded(prefix = "suspension_")
    val suspension: SuspensionEntity,
    @Embedded(prefix = "brakes_")
    val brakes: BrakesEntity,
    @Embedded(prefix = "tyres_")
    val tyres: TyresEntity,
    val notes: String?,
    val score: Double
)
