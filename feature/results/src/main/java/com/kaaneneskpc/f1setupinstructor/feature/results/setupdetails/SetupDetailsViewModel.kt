package com.kaaneneskpc.f1setupinstructor.feature.results.setupdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.f1setupinstructor.domain.repository.SetupRepository
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetupDetailsUiState())
    val uiState = _uiState.asStateFlow()

    // Navigation arguments - optional as this screen could be launched with or without them
    private val setupId: String? = savedStateHandle.get<String>("setupId")
    private val trackName: String? = savedStateHandle.get<String>("trackName")

    init {
        // Load setup if ID is provided
        setupId?.let { id ->
            loadSetupById(id)
        }
    }

    private fun loadSetupById(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            // TODO: Implement repository method to get setup by ID
            // For now, use a default state
            _uiState.update { it.copy(isLoading = false) }
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
                
                // TODO: Persist favorite state to repository
                viewModelScope.launch {
                    // setupRepository.updateFavoriteStatus(setupId, newFavoriteState)
                }
            }
            
            is SetupDetailsEvent.BackClicked -> {
                // Handled by navigation in the composable
            }
            
            is SetupDetailsEvent.ShareClicked -> {
                // TODO: Implement share functionality
                viewModelScope.launch {
                    // Create share intent with setup details
                }
            }
        }
    }
}
