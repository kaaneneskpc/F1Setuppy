package com.kaaneneskpc.f1setupinstructor.core.network

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

class ChatServiceImpl @Inject constructor(
    private val generativeModel: GenerativeModel
) : ChatService {
    
    override suspend fun sendMessage(message: String): Result<String> {
        return try {
            val prompt = createChatPrompt(message)
            val response = generativeModel.generateContent(prompt)
            val text = response.text ?: "Üzgünüm, bir cevap oluşturamadım."
            Result.success(text)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun sendMessageWithImage(message: String, imageBitmap: Bitmap): Result<String> {
        return try {
            val prompt = createImagePrompt(message)
            val inputContent = content {
                image(imageBitmap)
                text(prompt)
            }
            val response = generativeModel.generateContent(inputContent)
            val text = response.text ?: "Üzgünüm, görseli analiz edemedim."
            Result.success(text)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun createChatPrompt(userMessage: String): String {
        return """
        Sen bir F1 25 oyunu setup uzmanısın. Kullanıcının F1 setup'ları, pist stratejileri ve araba ayarları hakkındaki sorularını yanıtlıyorsun.
        
        ÖNEMLI TALİMATLAR:
        1. EA SPORTS F1 25 oyunu için öneriler ver (F1 24 veya F1 23 değil)
        2. Cevaplarını kısa ve öz tut (maksimum 200 kelime)
        3. Türkçe yanıt ver
        4. Setup değerleri verirken F1 25 oyununun aralıklarını kullan:
           - Aero: Ön/Arka 0-50 (düşük = az downforce)
           - Camber: -3.5° ile -1.0° arası (negatif değerler)
           - Toe: -0.50° ile 0.50° arası
           - Süspansiyon: 1-11 (yumuşak-sert)
           - Anti-Roll Bar: 1-11 (yumuşak-sert)
           - Ride Height: 0-100mm
           - Fren Basıncı: 80-100%
           - Fren Dengesi: 50-70% (ön bias)
           - Lastik Basıncı: 19.0-25.0 PSI
        5. Pratik ve uygulanabilir öneriler ver
        6. Gerekirse pist bazlı spesifik tavsiyeler sun
        
        KULLANICI SORUSU:
        $userMessage
        
        YANITINI VER (kısa, öz, Türkçe):
        """.trimIndent()
    }
    
    private fun createImagePrompt(userMessage: String): String {
        return """
        Sen bir F1 25 oyunu setup uzmanısın. Kullanıcı bir görsel paylaştı ve sana bir soru sordu.
        
        GÖREV:
        1. Görseli analiz et (setup ekranı, telemetri, oyun içi görüntü vs.)
        2. Görseldeki bilgileri F1 25 oyunu bağlamında yorumla
        3. Kullanıcının sorusunu görselle ilişkilendirerek yanıtla
        
        ÖNEMLI TALİMATLAR:
        1. EA SPORTS F1 25 oyunu için öneriler ver
        2. Görseldeki setup değerlerini, telemetri verilerini veya oyun içi durumu analiz et
        3. Cevaplarını kısa ve öz tut (maksimum 250 kelime)
        4. Türkçe yanıt ver
        5. Görselde gördüklerini açıkla ve öneriler sun
        6. Setup değerleri varsa, F1 25 standartlarına göre değerlendir
        
        KULLANICI MESAJI:
        ${if (userMessage.isNotBlank()) userMessage else "Bu görseli analiz edebilir misin?"}
        
        YANITINI VER (görseli analiz et, kısa, öz, Türkçe):
        """.trimIndent()
    }
}

