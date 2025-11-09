package com.kaaneneskpc.f1setupinstructor.feature.chatbot

sealed interface ChatEvent {
    data object OnBack : ChatEvent
    data class OnSuggestionClick(val text: String) : ChatEvent
    data class OnInputChange(val text: String) : ChatEvent
    data object OnAttachClick : ChatEvent
    data object OnSendClick : ChatEvent
    data class OnMessageLongPress(val id: String) : ChatEvent
    data class OnImageSelected(val uri: String) : ChatEvent
    data object OnClearImage : ChatEvent
}

