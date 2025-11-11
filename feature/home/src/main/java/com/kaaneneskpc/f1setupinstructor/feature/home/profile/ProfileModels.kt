package com.kaaneneskpc.f1setupinstructor.feature.home.profile

import androidx.compose.runtime.Stable

@Stable
data class ProfileUiState(
    val title: String = "Profile",
    val avatarUrl: String? = null,
    val name: String = "Kaan Enes KAPICI",
    val handle: String = "@kaaneneskpc",
    val email: String = "kaaneneskpc1@gmail.com",
    val notificationsEnabled: Boolean = true,
    val darkThemeEnabled: Boolean = true,
    val favoriteTracks: List<TrackRow> = emptyList(),
    val showNameDialog: Boolean = false,
    val showEmailDialog: Boolean = false,
    val showHandleDialog: Boolean = false,
    val showAddTrackDialog: Boolean = false,
    val showImagePickerDialog: Boolean = false
)

data class TrackRow(val name: String, val starred: Boolean)

sealed interface ProfileEvent {
    data object OnBack : ProfileEvent
    data object OnAvatarClick : ProfileEvent
    data object OnNameRowClick : ProfileEvent
    data object OnEmailRowClick : ProfileEvent
    data object OnHandleRowClick : ProfileEvent
    data class OnNotificationsToggle(val enabled: Boolean) : ProfileEvent
    data class OnDarkThemeToggle(val enabled: Boolean) : ProfileEvent
    data class OnTrackStarToggle(val name: String) : ProfileEvent
    data object OnAddTrackClick : ProfileEvent
    data class OnNameChanged(val value: String) : ProfileEvent
    data class OnEmailChanged(val value: String) : ProfileEvent
    data class OnHandleChanged(val value: String) : ProfileEvent
    data class OnNewTrackAdded(val trackName: String) : ProfileEvent
    data class OnAvatarImageSelected(val imageUri: String) : ProfileEvent
    data object OnImagePickerDismiss : ProfileEvent
    data object OnDialogDismiss : ProfileEvent
}

