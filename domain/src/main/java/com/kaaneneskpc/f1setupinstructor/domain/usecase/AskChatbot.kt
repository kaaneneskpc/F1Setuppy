package com.kaaneneskpc.f1setupinstructor.domain.usecase

import com.kaaneneskpc.f1setupinstructor.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface AskChatbot {
    operator fun invoke(question: String, context: String?): Flow<ChatMessage>
}
