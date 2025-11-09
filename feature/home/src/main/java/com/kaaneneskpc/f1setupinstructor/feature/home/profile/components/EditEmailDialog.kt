package com.kaaneneskpc.f1setupinstructor.feature.home.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EditEmailDialog(
    currentEmail: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var email by remember { mutableStateOf(currentEmail) }
    var isError by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF2D2D2D),
        title = {
            Text("E-posta Düzenle", color = Color.White)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        isError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                    },
                    label = { Text("E-posta Adresi", color = Color.Gray) },
                    singleLine = true,
                    isError = isError,
                    supportingText = if (isError) {
                        { Text("Geçersiz e-posta adresi", color = Color.Red) }
                    } else null,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.Red,
                        focusedContainerColor = Color(0xFF3D3D3D),
                        unfocusedContainerColor = Color(0xFF3D3D3D),
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.Gray,
                        errorBorderColor = Color.Red,
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (!isError) onConfirm(email) },
                enabled = !isError && email.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    disabledContainerColor = Color.Red.copy(alpha = 0.5f)
                )
            ) {
                Text("Kaydet", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal", color = Color.White)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

