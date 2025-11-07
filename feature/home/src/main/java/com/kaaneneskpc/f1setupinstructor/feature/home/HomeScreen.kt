package com.kaaneneskpc.f1setupinstructor.feature.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaaneneskpc.f1setupinstructor.core.ui.components.GradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var selectedQualifyingWeather by remember { mutableStateOf("Güneşli") }
    var selectedRaceWeather by remember { mutableStateOf("Bulutlu") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Yeni F1 Setup",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }

        item {
            Section(title = "Yarış Pisti") {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Pist adını girin...") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.Red,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Red,
                        unfocusedIndicatorColor = Color.Gray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }
        }

        item {
            Section(title = "Favori Pistler") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FavoriteTrackChip("Monza", true)
                    FavoriteTrackChip("Silverstone", true)
                    FavoriteTrackChip("Spa")
                    FavoriteTrackChip("Suzuka")
                }
            }
        }

        item {
            Section(title = "Hava Durumu") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    WeatherSelector(
                        title = "Sıralama Turu",
                        selectedWeather = selectedQualifyingWeather,
                        onWeatherSelected = { selectedQualifyingWeather = it }
                    )
                    WeatherSelector(
                        title = "Yarış",
                        selectedWeather = selectedRaceWeather,
                        onWeatherSelected = { selectedRaceWeather = it }
                    )
                }
            }
        }

        item {
            Section(title = "En Son Setup") {
                LastSetupCard()
            }
        }

        item {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Setup Önerisi Al", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun WeatherSelector(
    title: String,
    selectedWeather: String,
    onWeatherSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(title, color = Color.White)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WeatherConditionChip(
                iconRes = R.drawable.ic_sunny,
                text = "Güneşli",
                selected = selectedWeather == "Güneşli",
                onClick = { onWeatherSelected("Güneşli") }
            )
            WeatherConditionChip(
                iconRes = R.drawable.ic_cloudy,
                text = "Bulutlu",
                selected = selectedWeather == "Bulutlu",
                onClick = { onWeatherSelected("Bulutlu") }
            )
            WeatherConditionChip(
                iconRes = R.drawable.ic_rainy,
                text = "Yağmurlu",
                selected = selectedWeather == "Yağmurlu",
                onClick = { onWeatherSelected("Yağmurlu") }
            )
        }
    }
}


@Composable
fun Section(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        content()
    }
}

@Composable
fun FavoriteTrackChip(text: String, hasIcon: Boolean = false) {
    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.DarkGray.copy(alpha = 0.5f)
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        if (hasIcon) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(text, color = Color.White)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherConditionChip(
    @DrawableRes iconRes: Int,
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color.DarkGray.copy(alpha = 0.5f)
    val contentColor = if (selected) Color.Red else Color.Gray
    val border = if (selected) BorderStroke(1.dp, Color.Red) else null

    Card(
        modifier = modifier.size(80.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = border,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text, color = contentColor, fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    }
}


@Composable
fun LastSetupCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Monza", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("2 gün önce", color = Color.Gray, fontSize = 12.sp)
                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Tekrar Kullan", color = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SetupDetail(title = "Ön Kanat", value = "8")
                SetupDetail(title = "Arka Kanat", value = "9")
            }
        }
    }
}

@Composable
fun SetupDetail(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, color = Color.Gray, fontSize = 14.sp)
        Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GradientBackground {
        HomeScreen()
    }
}
