package com.kaaneneskpc.f1setupinstructor.core.network

import android.graphics.Bitmap

interface ChatService {
    suspend fun sendMessage(message: String): Result<String>
    suspend fun sendMessageWithImage(message: String, imageBitmap: Bitmap): Result<String>
}

