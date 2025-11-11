package com.kaaneneskpc.f1setupinstructor.feature.results.setupdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.f1setupinstructor.domain.repository.SetupRepository
import com.kaaneneskpc.f1setupinstructor.domain.repository.CachedSetupManager
import com.kaaneneskpc.f1setupinstructor.domain.model.Setup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupDetailsViewModel @Inject constructor(
    private val setupRepository: SetupRepository,
    private val cachedSetupManager: CachedSetupManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetupDetailsUiState())
    val uiState = _uiState.asStateFlow()


    private val setupId: String? = savedStateHandle.get<String>("setupId")
    private val trackName: String? = savedStateHandle.get<String>("trackName")

    init {
        val cachedSetup = cachedSetupManager.getLatestSetup()
        if (cachedSetup != null) {
            loadSetupData(cachedSetup)
        } else if (setupId != null) {
            loadSetupById(setupId)
        }
    }

    /**
     * Load setup from SetupData (from AI response)
     */
    private fun loadSetupData(setupData: com.kaaneneskpc.f1setupinstructor.domain.model.SetupData) {
        android.util.Log.d("SetupDetailsViewModel", "Loading setup: ${setupData.trackName} - ${setupData.carModel}")
        _uiState.update { 
            it.copy(
                imageUrl = setupData.imageUrl,
                badge = setupData.setupType.uppercase(),
                title = setupData.trackName,
                subtitle = "${setupData.gameVersion} - ${setupData.weatherCondition}",
                aerodynamics = AeroData(
                    frontWingAero = setupData.frontWingAero,
                    rearWingAero = setupData.rearWingAero
                ),
                transmission = TransmissionData(
                    onThrottle = setupData.onThrottle,
                    offThrottle = setupData.offThrottle,
                    engineBraking = setupData.engineBraking
                ),
                suspensionGeometry = SuspensionGeometryData(
                    frontCamber = setupData.frontCamber.toDouble(),
                    rearCamber = setupData.rearCamber.toDouble(),
                    frontToe = setupData.frontToe.toDouble(),
                    rearToe = setupData.rearToe.toDouble()
                ),
                suspension = SuspensionData(
                    frontSusp = setupData.frontSuspension,
                    rearSusp = setupData.rearSuspension,
                    frontARB = setupData.frontAntiRollBar,
                    rearARB = setupData.rearAntiRollBar,
                    frontRideHeight = setupData.frontRideHeight,
                    rearRideHeight = setupData.rearRideHeight
                ),
                brakes = BrakesData(
                    pressure = setupData.brakePressure,
                    bias = setupData.frontBrakeBias
                ),
                tyres = TyresData(
                    frontLeftPsi = setupData.frontLeftTyrePsi.toDouble(),
                    frontRightPsi = setupData.frontRightTyrePsi.toDouble(),
                    rearLeftPsi = setupData.rearLeftTyrePsi.toDouble(),
                    rearRightPsi = setupData.rearRightTyrePsi.toDouble()
                ),
                trackDetails = TrackDetails(
                    length = setupData.trackLength,
                    corners = setupData.trackCorners,
                    drsZones = setupData.trackDrsZones,
                    idealLaps = setupData.trackIdealLaps
                ),
                tyreStrategy = setupData.tyreStrategy,
                keyPointers = setupData.keyPointers,
                creatorNotes = setupData.creatorNotes
            )
        }
    }
    
    private fun loadSetupById(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val setup = setupRepository.getSetupDetail(id)
                _uiState.update { 
                    setup.toUiState().copy(isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Setup bulunamadı: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Load setup from domain model
     * This can be called from the composable if the Setup is passed via navigation
     */
    fun loadSetup(setup: Setup) {
        _uiState.update { 
            setup.toUiState().copy(
                selectedTabIndex = _uiState.value.selectedTabIndex,
                isFavorite = _uiState.value.isFavorite
            )
        }
    }

    fun onEvent(event: SetupDetailsEvent) {
        when (event) {
            is SetupDetailsEvent.TabSelected -> {
                _uiState.update { it.copy(selectedTabIndex = event.index) }
            }
            
            is SetupDetailsEvent.ToggleFavorite -> {
                val newFavoriteState = !_uiState.value.isFavorite
                _uiState.update { it.copy(isFavorite = newFavoriteState) }
                
                viewModelScope.launch {
                    try {
                        // Convert UI state back to domain model and save
                        // For now, just update local state
                        // In production, you would call:
                        // setupRepository.saveFavorite(currentSetup)
                    } catch (e: Exception) {
                        // Revert on error
                        _uiState.update { it.copy(isFavorite = !newFavoriteState) }
                    }
                }
            }
            
            is SetupDetailsEvent.BackClicked -> {
                // Handled by navigation in the composable
            }
            
            is SetupDetailsEvent.ShareClicked -> {
                // Share functionality
                // This will be handled by the composable with Android's share intent
                // The ViewModel just logs the event for analytics
                viewModelScope.launch {
                    // Log share event for analytics
                    // Analytics.logEvent("setup_shared", mapOf("track" to trackName))
                }
            }
        }
    }
}
