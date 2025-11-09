package com.kaaneneskpc.f1setupinstructor.feature.home.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.f1setupinstructor.domain.model.UserProfile
import com.kaaneneskpc.f1setupinstructor.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    init {
        loadProfile()
        observePreferences()
    }
    
    private fun loadProfile() {
        viewModelScope.launch {
            val profile = profileRepository.getUserProfile()
            _uiState.update { state ->
                state.copy(
                    name = profile.name,
                    handle = profile.handle,
                    email = profile.email,
                    avatarUrl = profile.avatarUrl,
                    favoriteTracks = profile.favoriteTracks.map { TrackRow(it, starred = true) }
                )
            }
        }
    }
    
    private fun observePreferences() {
        viewModelScope.launch {
            profileRepository.notificationsEnabled.collect { enabled ->
                _uiState.update { it.copy(notificationsEnabled = enabled) }
            }
        }
        
        viewModelScope.launch {
            profileRepository.darkThemeEnabled.collect { enabled ->
                _uiState.update { it.copy(darkThemeEnabled = enabled) }
            }
        }
    }
    
    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnAvatarClick -> handleAvatarClick()
            is ProfileEvent.OnNameRowClick -> _uiState.update { it.copy(showNameDialog = true) }
            is ProfileEvent.OnEmailRowClick -> _uiState.update { it.copy(showEmailDialog = true) }
            is ProfileEvent.OnHandleRowClick -> _uiState.update { it.copy(showHandleDialog = true) }
            is ProfileEvent.OnNotificationsToggle -> updateNotifications(event.enabled)
            is ProfileEvent.OnDarkThemeToggle -> updateDarkTheme(event.enabled)
            is ProfileEvent.OnTrackStarToggle -> toggleTrackStar(event.name)
            is ProfileEvent.OnAddTrackClick -> _uiState.update { it.copy(showAddTrackDialog = true) }
            is ProfileEvent.OnNameChanged -> updateName(event.value)
            is ProfileEvent.OnEmailChanged -> updateEmail(event.value)
            is ProfileEvent.OnHandleChanged -> updateHandle(event.value)
            is ProfileEvent.OnNewTrackAdded -> addTrack(event.trackName)
            is ProfileEvent.OnDialogDismiss -> dismissDialogs()
            else -> {}
        }
    }
    
    private fun handleAvatarClick() {
        // TODO: Open image picker for avatar
    }
    
    private fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch {
            profileRepository.setNotificationsEnabled(enabled)
        }
    }
    
    private fun updateDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            profileRepository.setDarkThemeEnabled(enabled)
        }
    }
    
    private fun toggleTrackStar(trackName: String) {
        _uiState.update { state ->
            val updatedTracks = state.favoriteTracks.map { track ->
                if (track.name == trackName) {
                    track.copy(starred = !track.starred)
                } else {
                    track
                }
            }
            state.copy(favoriteTracks = updatedTracks)
        }
        saveProfileToRepository()
    }
    
    private fun updateName(name: String) {
        _uiState.update { it.copy(name = name, showNameDialog = false) }
        saveProfileToRepository()
    }
    
    private fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, showEmailDialog = false) }
        saveProfileToRepository()
    }
    
    private fun updateHandle(handle: String) {
        _uiState.update { it.copy(handle = handle, showHandleDialog = false) }
        saveProfileToRepository()
    }
    
    private fun addTrack(trackName: String) {
        _uiState.update { state ->
            val newTrack = TrackRow(trackName, starred = true)
            state.copy(
                favoriteTracks = state.favoriteTracks + newTrack,
                showAddTrackDialog = false
            )
        }
        saveProfileToRepository()
    }
    
    private fun saveProfileToRepository() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val profile = UserProfile(
                name = currentState.name,
                handle = currentState.handle,
                email = currentState.email,
                avatarUrl = currentState.avatarUrl,
                favoriteTracks = currentState.favoriteTracks
                    .filter { it.starred }
                    .map { it.name }
            )
            profileRepository.updateProfile(profile)
        }
    }
    
    private fun dismissDialogs() {
        _uiState.update { it.copy(
            showNameDialog = false,
            showEmailDialog = false,
            showHandleDialog = false,
            showAddTrackDialog = false
        ) }
    }
}

