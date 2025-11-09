package com.kaaneneskpc.f1setupinstructor.feature.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.f1setupinstructor.core.network.ChatService
import com.kaaneneskpc.f1setupinstructor.domain.model.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val chatService: ChatService
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatUiState(
        messages = listOf(
            ChatMessage.ai("Merhaba! F1 setup'ınla ilgili nasıl yardımcı olabilirim? Pist, hava durumu ve yaşadığın sorun hakkında bilgi verebilir misin?")
        )
    ))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnInputChange -> handleInputChange(event.text)
            is ChatEvent.OnSendClick -> handleSendMessage()
            is ChatEvent.OnSuggestionClick -> handleSuggestionClick(event.text)
            is ChatEvent.OnAttachClick -> handleAttach()
            is ChatEvent.OnMessageLongPress -> handleMessageLongPress(event.id)
            else -> {} // OnBack is handled in the Route
        }
    }
    
    private fun handleInputChange(text: String) {
        _uiState.update { it.copy(
            input = text,
            canSend = text.isNotBlank()
        ) }
    }
    
    private fun handleSendMessage() {
        val input = _uiState.value.input.trim()
        if (input.isBlank()) return
        
        // Add user message
        val userMessage = ChatMessage.user(input)
        _uiState.update { state ->
            state.copy(
                messages = state.messages + userMessage,
                input = "",
                canSend = false,
                isTyping = true,
                suggestions = emptyList() // Clear suggestions after first message
            )
        }
        
        // Get AI response using Gemini
        viewModelScope.launch {
            chatService.sendMessage(input)
                .onSuccess { response ->
                    _uiState.update { state ->
                        state.copy(
                            messages = state.messages + ChatMessage.ai(response),
                            isTyping = false
                        )
                    }
                }
                .onFailure { exception ->
                    // Fallback to local response on error
                    val fallbackResponse = "Üzgünüm, şu anda bir sorun yaşıyorum. Lütfen tekrar deneyin.\n\nHata: ${exception.message}"
                    _uiState.update { state ->
                        state.copy(
                            messages = state.messages + ChatMessage.ai(fallbackResponse),
                            isTyping = false
                        )
                    }
                }
        }
    }
    
    private fun handleSuggestionClick(text: String) {
        _uiState.update { it.copy(
            input = text,
            canSend = true
        ) }
    }
    
    private fun handleAttach() {
        // TODO: Implement attachment functionality
        // Could be used to attach setup files, telemetry data, etc.
    }
    
    private fun handleMessageLongPress(id: String) {
        // TODO: Show dialog with Copy/Share/Report options
        // For now, just a stub
    }
}
