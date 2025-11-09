package com.kaaneneskpc.f1setupinstructor.domain.model

data class UserProfile(
    val name: String,
    val handle: String,
    val email: String,
    val avatarUrl: String?,
    val favoriteTracks: List<String>
)

