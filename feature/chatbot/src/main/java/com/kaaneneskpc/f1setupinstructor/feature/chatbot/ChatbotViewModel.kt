package com.kaaneneskpc.f1setupinstructor.feature.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.f1setupinstructor.domain.model.ChatMessage
import com.kaaneneskpc.f1setupinstructor.domain.usecase.AskChatbot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val askChatbot: AskChatbot
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun sendMessage(message: String) {
        viewModelScope.launch {
            askChatbot(message, null).collectLatest {
                _messages.value = _messages.value + it
            }
        }
    }
}
