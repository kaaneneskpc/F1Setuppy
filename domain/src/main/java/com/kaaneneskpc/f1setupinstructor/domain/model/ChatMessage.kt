package com.kaaneneskpc.f1setupinstructor.domain.model

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: Role,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUri: String? = null
) {
    companion object {
        fun ai(text: String) = ChatMessage(role = Role.AI, text = text)
        fun user(text: String, imageUri: String? = null) = ChatMessage(role = Role.USER, text = text, imageUri = imageUri)
    }
}

enum class Role {
    AI,
    USER
}
