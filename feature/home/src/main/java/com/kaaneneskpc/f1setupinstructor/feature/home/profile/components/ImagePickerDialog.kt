package com.kaaneneskpc.f1setupinstructor.feature.home.profile.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ImagePickerDialog(
    onImageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            uri?.let { 
                onImageSelected(it.toString())
            } ?: onDismiss()
        }
    )
    
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                // Camera image saved to the provided URI
                // You'll need to pass a URI to save the camera image
                // For now, we'll just dismiss
                onDismiss()
            } else {
                onDismiss()
            }
        }
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF2D2D2D),
        title = {
            Text("Profil Fotoğrafı Seç", color = Color.White)
        },
        text = {
            Column {
                Text(
                    "Profil fotoğrafınızı nasıl güncellemek istersiniz?",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Galeri",
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text("Galeriden Seç")
                    Spacer(modifier = Modifier.weight(1f))
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedButton(
                    onClick = {
                        // Note: For camera, you need to create a URI to save the image
                        // This is a simplified version, you might want to implement proper URI handling
                        // cameraLauncher.launch(uri)
                        onDismiss() // For now, just dismiss
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Kamera",
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text("Kamera ile Çek")
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal", color = Color.White)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

