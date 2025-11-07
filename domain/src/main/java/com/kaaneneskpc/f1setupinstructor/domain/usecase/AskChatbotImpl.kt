package com.kaaneneskpc.f1setupinstructor.domain.usecase

import com.kaaneneskpc.f1setupinstructor.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AskChatbotImpl @Inject constructor() : AskChatbot {
    override fun invoke(question: String, context: String?): Flow<ChatMessage> = flow {
        emit(ChatMessage("This is a mocked response to your question: '$question'"))
    }
}
