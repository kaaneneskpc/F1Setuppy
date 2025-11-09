package com.kaaneneskpc.f1setupinstructor.core.network

interface ChatService {
    suspend fun sendMessage(message: String): Result<String>
}

