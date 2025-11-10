package com.kaaneneskpc.f1setupinstructor.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.f1setupinstructor.domain.repository.SetupRepository
import com.kaaneneskpc.f1setupinstructor.domain.repository.CachedSetupManager
import com.kaaneneskpc.f1setupinstructor.domain.repository.HistoryRepository
import com.kaaneneskpc.f1setupinstructor.domain.model.HistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val setupRepository: SetupRepository,
    private val cachedSetupManager: CachedSetupManager,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.TrackChanged -> {
                uiState = uiState.copy(track = event.track)
            }
            is HomeEvent.SetupTypeChanged -> {
                uiState = uiState.copy(setupType = event.setupType)
            }
            is HomeEvent.QualyWeatherChanged -> {
                uiState = uiState.copy(qualyWeather = event.weather)
            }
            is HomeEvent.RaceWeatherChanged -> {
                uiState = uiState.copy(raceWeather = event.weather)
            }
            HomeEvent.GetSetupClicked -> {
                getSetup()
            }
            HomeEvent.DismissError -> {
                uiState = uiState.copy(error = null)
            }
        }
    }

    private fun getSetup() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            
            try {
                val result = setupRepository.getBestSetup(
                    track = uiState.track,
                    setupType = uiState.setupType,
                    qualyWeather = uiState.qualyWeather,
                    raceWeather = uiState.raceWeather
                )
                
                result.onSuccess { setupData ->
                    // Cache the setup data for SetupDetailsScreen
                    cachedSetupManager.saveLatestSetup(setupData)
                    
                    // Save to history
                    val historyItem = HistoryItem(
                        timestamp = Instant.now(),
                        circuit = setupData.trackName,
                        weatherQuali = if (uiState.setupType == "QUALIFYING") uiState.qualyWeather else uiState.raceWeather,
                        weatherRace = if (uiState.setupType == "RACE") uiState.raceWeather else uiState.qualyWeather,
                        selectedSetupId = setupData.trackName, // Use trackName as ID for now
                        isFavorite = false
                    )
                    historyRepository.insertHistory(historyItem)
                    
                    uiState = uiState.copy(isLoading = false)
                    _navigationEvent.send(NavigationEvent.NavigateToSetupDetails(setupData.trackName))
                }.onFailure { exception ->
                    val errorMessage = buildErrorMessage(exception)
                    uiState = uiState.copy(isLoading = false, error = errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = buildErrorMessage(e)
                uiState = uiState.copy(isLoading = false, error = errorMessage)
            }
        }
    }
    
    private fun buildErrorMessage(exception: Throwable): String {
        return when {
            exception.message?.contains("MAX_TOKENS") == true ||
            exception.message?.contains("ResponseStopped") == true -> {
                "⚠️ Token Limiti Aşıldı\n\n" +
                "AI yanıtı çok uzun oldu ve yarıda kesildi.\n\n" +
                "Çözüm:\n" +
                "• Hemen tekrar deneyin (genelde 2. denemede düzelir)\n" +
                "• AI artık daha kısa yanıt verecek şekilde ayarlandı\n\n" +
                "Bu normal bir durum, endişelenmeyin!"
            }
            exception.message?.contains("API key") == true -> {
                "❌ API Key Hatası\n\n" +
                "Gemini API key yapılandırılmamış!\n\n" +
                "Çözüm:\n" +
                "1. https://makersuite.google.com/app/apikey adresine git\n" +
                "2. API key oluştur\n" +
                "3. local.properties dosyasına ekle:\n" +
                "   GEMINI_API_KEY=your_key_here\n" +
                "4. Gradle sync yap"
            }
            exception.message?.contains("UnknownHost") == true || 
            exception.message?.contains("Unable to resolve") == true -> {
                "🌐 İnternet Bağlantısı Hatası\n\n" +
                "İnternet bağlantınızı kontrol edin.\n\n" +
                "Detay: ${exception.message}"
            }
            exception.message?.contains("timeout") == true -> {
                "⏱️ Zaman Aşımı\n\n" +
                "AI yanıt vermesi çok uzun sürdü.\n\n" +
                "Tekrar deneyin veya internet bağlantınızı kontrol edin."
            }
            exception.message?.contains("429") == true || 
            exception.message?.contains("rate limit") == true -> {
                "🚫 Rate Limit Aşıldı\n\n" +
                "Çok fazla istek gönderdiniz.\n\n" +
                "1 dakika bekleyip tekrar deneyin.\n\n" +
                "Ücretsiz limit: 15 istek/dakika"
            }
            exception.message?.contains("JSON") == true || 
            exception.message?.contains("parse") == true ||
            exception.message?.contains("Serialization") == true ||
            exception.message?.contains("deserialize") == true -> {
                "📝 AI Yanıt Formatı Hatası\n\n" +
                "AI, yanıtı doğru JSON formatında döndürmedi.\n\n" +
                "Ne yapmalı:\n" +
                "1. Tekrar deneyin (AI bazen hata yapabiliyor)\n" +
                "2. Farklı bir pist adı deneyin\n" +
                "3. Logcat'te 'ResearchServiceImpl' filtresi ile tam yanıtı görün\n\n" +
                "Not: AI öğreniyor, birkaç deneme gerekebilir.\n\n" +
                "Detay: ${exception.message?.take(100)}"
            }
            exception.message?.contains("401") == true || 
            exception.message?.contains("403") == true -> {
                "🔒 Yetkilendirme Hatası\n\n" +
                "API key geçersiz veya izinler yetersiz.\n\n" +
                "API key'inizi kontrol edin.\n\n" +
                "Detay: ${exception.message}"
            }
            else -> {
                "❌ Setup Oluşturulamadı\n\n" +
                "Hata: ${exception.message ?: "Bilinmeyen hata"}\n\n" +
                "Detay: ${exception.javaClass.simpleName}\n\n" +
                "Lütfen tekrar deneyin veya farklı bir pist deneyin."
            }
        }
    }
}

data class HomeUiState(
    val track: String = "",
    val setupType: String = "RACE", // QUALIFYING or RACE
    val qualyWeather: String = "Dry",
    val raceWeather: String = "Dry",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface HomeEvent {
    data class TrackChanged(val track: String) : HomeEvent
    data class SetupTypeChanged(val setupType: String) : HomeEvent
    data class QualyWeatherChanged(val weather: String) : HomeEvent
    data class RaceWeatherChanged(val weather: String) : HomeEvent
    object GetSetupClicked : HomeEvent
    object DismissError : HomeEvent
}

sealed interface NavigationEvent {
    data class NavigateToSetupDetails(val trackName: String) : NavigationEvent
}
