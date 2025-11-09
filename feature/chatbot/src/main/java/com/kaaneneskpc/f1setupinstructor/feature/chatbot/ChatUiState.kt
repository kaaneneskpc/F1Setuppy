package com.kaaneneskpc.f1setupinstructor.feature.chatbot

import androidx.compose.runtime.Stable
import com.kaaneneskpc.f1setupinstructor.domain.model.ChatMessage

@Stable
data class ChatUiState(
    val title: String = "Setup Asistanı",
    val messages: List<ChatMessage> = emptyList(),
    val input: String = "",
    val suggestions: List<String> = listOf(
        "Viraj çıkışında araba kayıyor",
        "Düzlük hızı nasıl artırılır?"
    ),
    val isTyping: Boolean = false,
    val canSend: Boolean = false
)

