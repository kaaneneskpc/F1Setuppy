package com.kaaneneskpc.f1setupinstructor.feature.home.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaaneneskpc.f1setupinstructor.core.ui.components.GradientBackground
import com.kaaneneskpc.f1setupinstructor.feature.home.profile.components.AddTrackDialog
import com.kaaneneskpc.f1setupinstructor.feature.home.profile.components.AddTrackRow
import com.kaaneneskpc.f1setupinstructor.feature.home.profile.components.AvatarHeader
import com.kaaneneskpc.f1setupinstructor.feature.home.profile.components.EditEmailDialog
import com.kaaneneskpc.f1setupinstructor.feature.home.profile.components.EditHandleDialog
import com.kaaneneskpc.f1setupinstructor.feature.home.profile.components.EditNameDialog
import com.kaaneneskpc.f1setupinstructor.feature.home.profile.components.ImagePickerDialog
import com.kaaneneskpc.f1setupinstructor.feature.home.profile.components.ProfileTopBar
import com.kaaneneskpc.f1setupinstructor.feature.home.profile.components.SectionHeader
import com.kaaneneskpc.f1setupinstructor.feature.home.profile.components.SettingRow
import com.kaaneneskpc.f1setupinstructor.feature.home.profile.components.SwitchRow
import com.kaaneneskpc.f1setupinstructor.feature.home.profile.components.TrackRowItem

@Composable
fun ProfileRoute(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    ProfileScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                ProfileEvent.OnBack -> onBack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit
) {
    ProfileContent(
        uiState = uiState,
        onEvent = onEvent
    )
}

@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit
) {
    GradientBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                ProfileTopBar(
                    title = uiState.title,
                    onBackClick = { onEvent(ProfileEvent.OnBack) }
                )
            }

            item {
                AvatarHeader(
                    avatarUrl = uiState.avatarUrl,
                    name = uiState.name,
                    handle = uiState.handle,
                    onAvatarClick = { onEvent(ProfileEvent.OnAvatarClick) }
                )
            }

            item {
                SectionHeader("KİŞİSEL BİLGİLER")
            }
            
            item {
                PersonalInformationCard(
                    name = uiState.name,
                    email = uiState.email,
                    handle = uiState.handle,
                    onNameClick = { onEvent(ProfileEvent.OnNameRowClick) },
                    onEmailClick = { onEvent(ProfileEvent.OnEmailRowClick) },
                    onHandleClick = { onEvent(ProfileEvent.OnHandleRowClick) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeader("UYGULAMA AYARLARI")
            }
            
            item {
                AppSettingsCard(
                    notificationsEnabled = uiState.notificationsEnabled,
                    darkThemeEnabled = uiState.darkThemeEnabled,
                    onNotificationsToggle = { onEvent(ProfileEvent.OnNotificationsToggle(it)) },
                    onDarkThemeToggle = { onEvent(ProfileEvent.OnDarkThemeToggle(it)) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeader("FAVORİ PİSTLER")
            }
            
            item {
                FavoriteTracksCard(
                    favoriteTracks = uiState.favoriteTracks,
                    onTrackStarToggle = { onEvent(ProfileEvent.OnTrackStarToggle(it)) },
                    onAddTrackClick = { onEvent(ProfileEvent.OnAddTrackClick) }
                )
            }
        }

        if (uiState.showNameDialog) {
            EditNameDialog(
                currentName = uiState.name,
                onConfirm = { onEvent(ProfileEvent.OnNameChanged(it)) },
                onDismiss = { onEvent(ProfileEvent.OnDialogDismiss) }
            )
        }
        
        if (uiState.showEmailDialog) {
            EditEmailDialog(
                currentEmail = uiState.email,
                onConfirm = { onEvent(ProfileEvent.OnEmailChanged(it)) },
                onDismiss = { onEvent(ProfileEvent.OnDialogDismiss) }
            )
        }
        
        if (uiState.showHandleDialog) {
            EditHandleDialog(
                currentHandle = uiState.handle,
                onConfirm = { onEvent(ProfileEvent.OnHandleChanged(it)) },
                onDismiss = { onEvent(ProfileEvent.OnDialogDismiss) }
            )
        }
        
        if (uiState.showAddTrackDialog) {
            AddTrackDialog(
                onConfirm = { onEvent(ProfileEvent.OnNewTrackAdded(it)) },
                onDismiss = { onEvent(ProfileEvent.OnDialogDismiss) }
            )
        }
        
        if (uiState.showImagePickerDialog) {
            ImagePickerDialog(
                onImageSelected = { uri -> onEvent(ProfileEvent.OnAvatarImageSelected(uri)) },
                onDismiss = { onEvent(ProfileEvent.OnImagePickerDismiss) }
            )
        }
    }
}

@Composable
private fun PersonalInformationCard(
    name: String,
    email: String,
    handle: String,
    onNameClick: () -> Unit,
    onEmailClick: () -> Unit,
    onHandleClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray.copy(alpha = 0.3f)
        )
    ) {
        SettingRow(
            icon = Icons.Default.Person,
            title = "Ad Soyad",
            subtitle = name,
            onClick = onNameClick
        )
        
        SettingRow(
            icon = Icons.Default.Email,
            title = "E-posta Adresi",
            subtitle = email,
            onClick = onEmailClick
        )
        
        SettingRow(
            icon = Icons.Default.Person,
            title = "Handle Değiştir",
            subtitle = handle,
            onClick = onHandleClick
        )
    }
}

@Composable
private fun AppSettingsCard(
    notificationsEnabled: Boolean,
    darkThemeEnabled: Boolean,
    onNotificationsToggle: (Boolean) -> Unit,
    onDarkThemeToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray.copy(alpha = 0.3f)
        )
    ) {
        SwitchRow(
            icon = Icons.Default.Notifications,
            title = "Bildirimler",
            checked = notificationsEnabled,
            onCheckedChange = onNotificationsToggle
        )
        
        SwitchRow(
            icon = Icons.Default.DarkMode,
            title = "Koyu Tema",
            checked = darkThemeEnabled,
            onCheckedChange = onDarkThemeToggle
        )
    }
}

@Composable
private fun FavoriteTracksCard(
    favoriteTracks: List<TrackRow>,
    onTrackStarToggle: (String) -> Unit,
    onAddTrackClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray.copy(alpha = 0.3f)
        )
    ) {
        favoriteTracks.filter { it.starred }.forEach { track ->
            TrackRowItem(
                trackName = track.name,
                starred = track.starred,
                onStarToggle = { onTrackStarToggle(track.name) }
            )
        }
        
        AddTrackRow(
            onClick = onAddTrackClick
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        uiState = ProfileUiState(
            favoriteTracks = listOf(
                TrackRow("Silverstone", starred = true),
                TrackRow("Monza", starred = true)
            )
        ),
        onEvent = {}
    )
}

