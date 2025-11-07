package com.kaaneneskpc.f1setupinstructor.core.database.entity

import java.time.Instant

data class SourceMetaEntity(
    val name: String,
    val url: String,
    val publishedAt: Instant,
    val communityRating: Double?
)
