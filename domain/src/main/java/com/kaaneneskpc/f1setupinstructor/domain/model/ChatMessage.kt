package com.kaaneneskpc.f1setupinstructor.domain.model

data class ChatMessage(
    val message: String,
    val setups: List<Setup> = emptyList()
)
