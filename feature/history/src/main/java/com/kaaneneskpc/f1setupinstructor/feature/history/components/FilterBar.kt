package com.kaaneneskpc.f1setupinstructor.feature.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FilterBar(
    onSortClick: () -> Unit,
    onCircuitClick: () -> Unit,
    onCarClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = false,
            onClick = onSortClick,
            label = { Text("Sırala", color = Color.White) },
            leadingIcon = {
                Icon(
                    Icons.Default.CalendarToday,
                    "Sort",
                    tint = Color.White
                )
            },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color.DarkGray.copy(alpha = 0.3f),
                labelColor = Color.White,
                iconColor = Color.White
            )
        )
        FilterChip(
            selected = false,
            onClick = onCircuitClick,
            label = { Text("Pist", color = Color.White) },
            leadingIcon = {
                Icon(
                    Icons.Default.LocationOn,
                    "Track",
                    tint = Color.White
                )
            },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color.DarkGray.copy(alpha = 0.3f),
                labelColor = Color.White,
                iconColor = Color.White
            )
        )
        FilterChip(
            selected = false,
            onClick = onCarClick,
            label = { Text("Araç", color = Color.White) },
            leadingIcon = {
                Icon(
                    Icons.Default.DirectionsCar,
                    "Car",
                    tint = Color.White
                )
            },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color.DarkGray.copy(alpha = 0.3f),
                labelColor = Color.White,
                iconColor = Color.White
            )
        )
    }
}

