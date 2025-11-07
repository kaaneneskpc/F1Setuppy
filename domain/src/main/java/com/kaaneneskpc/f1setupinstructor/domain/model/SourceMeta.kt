package com.kaaneneskpc.f1setupinstructor.domain.model

import java.time.Instant

data class SourceMeta(
    val name: String,
    val url: String,
    val publishedAt: Instant,
    val communityRating: Double?
)
