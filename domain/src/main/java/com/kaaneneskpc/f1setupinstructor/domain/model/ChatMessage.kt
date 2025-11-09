package com.kaaneneskpc.f1setupinstructor.domain.model

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: Role,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        fun ai(text: String) = ChatMessage(role = Role.AI, text = text)
        fun user(text: String) = ChatMessage(role = Role.USER, text = text)
    }
}

enum class Role {
    AI,
    USER
}
