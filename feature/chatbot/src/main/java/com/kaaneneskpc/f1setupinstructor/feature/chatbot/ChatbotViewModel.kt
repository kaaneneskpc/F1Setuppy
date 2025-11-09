package com.kaaneneskpc.f1setupinstructor.feature.chatbot

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.f1setupinstructor.core.network.ChatService
import com.kaaneneskpc.f1setupinstructor.domain.model.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val chatService: ChatService,
    @ApplicationContext private val context: Context
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
            is ChatEvent.OnAttachClick -> {} // Handled in UI with launcher
            is ChatEvent.OnMessageLongPress -> handleMessageLongPress(event.id)
            is ChatEvent.OnImageSelected -> handleImageSelected(event.uri)
            is ChatEvent.OnClearImage -> handleClearImage()
            else -> {} // OnBack is handled in the Route
        }
    }
    
    private fun handleInputChange(text: String) {
        _uiState.update { 
            it.copy(
                input = text,
                canSend = text.isNotBlank() || it.selectedImageUri != null
            ) 
        }
    }
    
    private fun handleSendMessage() {
        val input = _uiState.value.input.trim()
        val imageUri = _uiState.value.selectedImageUri
        
        if (input.isBlank() && imageUri == null) return
        
        // Add user message
        val userMessage = ChatMessage.user(input.ifBlank { "Bu görseli analiz edebilir misin?" }, imageUri)
        _uiState.update { state ->
            state.copy(
                messages = state.messages + userMessage,
                input = "",
                selectedImageUri = null,
                canSend = false,
                isTyping = true,
                suggestions = emptyList() // Clear suggestions after first message
            )
        }
        
        // Get AI response using Gemini
        viewModelScope.launch {
            val result = if (imageUri != null) {
                val bitmap = uriToBitmap(imageUri)
                if (bitmap != null) {
                    chatService.sendMessageWithImage(input, bitmap)
                } else {
                    Result.failure(Exception("Görsel yüklenemedi"))
                }
            } else {
                chatService.sendMessage(input)
            }
            
            result
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
    
    private fun handleImageSelected(uri: String) {
        _uiState.update { 
            it.copy(
                selectedImageUri = uri,
                canSend = true
            ) 
        }
    }
    
    private fun handleClearImage() {
        _uiState.update { 
            it.copy(
                selectedImageUri = null,
                canSend = it.input.isNotBlank()
            ) 
        }
    }
    
    private fun handleMessageLongPress(id: String) {
        // TODO: Show dialog with Copy/Share/Report options
        // For now, just a stub
    }
    
    private fun uriToBitmap(uriString: String): Bitmap? {
        return try {
            val uri = Uri.parse(uriString)
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            null
        }
    }
}
